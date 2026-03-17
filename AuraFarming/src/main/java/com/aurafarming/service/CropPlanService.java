package com.aurafarming.service;

import com.aurafarming.dao.CropPlanDAO;
import com.aurafarming.model.CropPlan;
import com.aurafarming.model.Season;
import com.aurafarming.model.User;
import com.aurafarming.util.IdGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class CropPlanService {
    private final CropPlanDAO cropPlanDAO = new CropPlanDAO();
    private final AuditService auditService = new AuditService();

    public int recommendationScore(String cropType, Season season) {
        String crop = cropType == null ? "" : cropType.toLowerCase(Locale.ROOT);
        return switch (season) {
            case MONSOON -> crop.contains("rice") ? 90 : 45;
            case WINTER -> crop.contains("wheat") || crop.contains("mustard") ? 88 : 40;
            case SPRING -> crop.contains("maize") ? 82 : 55;
            case SUMMER -> crop.contains("vegetable") ? 80 : 50;
            case AUTUMN -> crop.contains("millet") ? 78 : 52;
        };
    }

    public CropPlan savePlan(User actor, String farmerId, String plotId, String cropType, Season season,
            LocalDate startDate, LocalDate expectedHarvestDate, int score) {
        CropPlan plan = new CropPlan(IdGenerator.next("PLN"), farmerId, plotId, cropType, season, score, startDate,
                expectedHarvestDate);
        List<CropPlan> plans = cropPlanDAO.findAll();
        plans.add(plan);
        cropPlanDAO.saveAll(plans);
        auditService.log(actor, "SAVE_CROP_PLAN", "CropPlan", plan.getPlanId(), "Crop plan saved.");
        return plan;
    }

    public List<CropPlan> findAll() {
        return cropPlanDAO.findAll();
    }
}
