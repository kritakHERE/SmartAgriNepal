package com.aurafarming.service;

import com.aurafarming.dto.ExportRequestDTO;
import com.aurafarming.model.*;
import com.aurafarming.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExportService {
    private final AuditService auditService = new AuditService();
    private final YieldLogService yieldLogService = new YieldLogService();
    private final MarketPriceService marketPriceService = new MarketPriceService();
    private final CropPlanService cropPlanService = new CropPlanService();

    public Path export(User actor, ExportRequestDTO request) {
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());
        String fileName = request.reportType() + "_" + timestamp + ".csv";
        Path output = FileUtil.resolveExportFile(fileName);

        List<String> lines = switch (request.reportType()) {
            case "audit" -> exportAudit(request.fromDate(), request.toDate(), actor.getRole());
            case "yield" -> exportYield(actor);
            case "market" -> exportMarket(request.fromDate(), request.toDate());
            default -> exportCropPlan();
        };

        try {
            Files.write(output, lines);
        } catch (IOException e) {
            throw new RuntimeException("Unable to export file.", e);
        }

        auditService.log(actor, "EXPORT_REPORT", "Export", output.getFileName().toString(), "Export generated.");
        return output;
    }

    private List<String> exportAudit(LocalDate from, LocalDate to, Role role) {
        List<String> lines = new ArrayList<>();
        lines.add("logId,actorId,role,action,targetType,targetId,timestamp,details");
        for (AuditLog log : auditService.filter("", "ALL", from, to, role)) {
            lines.add(String.join(",", log.getLogId(), log.getActorId(), log.getActorRole().name(), log.getAction(),
                    log.getTargetType(), log.getTargetId(), log.getTimestamp().toString(), safe(log.getDetails())));
        }
        return lines;
    }

    private List<String> exportYield(User actor) {
        List<String> lines = new ArrayList<>();
        lines.add("yieldId,farmerId,plotId,district,cropType,estimatedKg,actualKg,harvestDate");
        for (YieldLog y : yieldLogService.findVisible(actor)) {
            lines.add(String.join(",", y.getYieldId(), y.getFarmerId(), y.getPlotId(), y.getDistrict().name(),
                    y.getCropType(), String.valueOf(y.getEstimatedKg()), String.valueOf(y.getActualKg()),
                    y.getHarvestDate().toString()));
        }
        return lines;
    }

    private List<String> exportMarket(LocalDate from, LocalDate to) {
        List<String> lines = new ArrayList<>();
        lines.add("priceId,district,cropType,pricePerKg,date");
        for (MarketPrice p : marketPriceService.findHistory(from, to, "")) {
            lines.add(String.join(",", p.getPriceId(), p.getDistrict().name(), p.getCropType(),
                    String.valueOf(p.getPricePerKg()), p.getDate().toString()));
        }
        return lines;
    }

    private List<String> exportCropPlan() {
        List<String> lines = new ArrayList<>();
        lines.add("planId,farmerId,plotId,cropType,season,recommendationScore,startDate,expectedHarvestDate");
        for (CropPlan p : cropPlanService.findAll()) {
            lines.add(String.join(",", p.getPlanId(), p.getFarmerId(), p.getPlotId(), p.getCropType(),
                    p.getSeason().name(),
                    String.valueOf(p.getRecommendationScore()), p.getStartDate().toString(),
                    p.getExpectedHarvestDate().toString()));
        }
        return lines;
    }

    private String safe(String value) {
        return value == null ? "" : value.replace(",", " ");
    }
}
