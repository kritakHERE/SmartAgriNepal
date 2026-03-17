package com.aurafarming.service;

import com.aurafarming.dao.YieldLogDAO;
import com.aurafarming.model.District;
import com.aurafarming.model.Role;
import com.aurafarming.model.User;
import com.aurafarming.model.YieldLog;
import com.aurafarming.util.IdGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class YieldLogService {
    private final YieldLogDAO yieldLogDAO = new YieldLogDAO();
    private final AuditService auditService = new AuditService();

    public YieldLog save(User actor, String farmerId, String plotId, District district, String cropType,
            double estimated, double actual, LocalDate date) {
        YieldLog log = new YieldLog(IdGenerator.next("YLD"), farmerId, plotId, district, cropType, estimated, actual,
                date);
        List<YieldLog> logs = yieldLogDAO.findAll();
        logs.add(log);
        yieldLogDAO.saveAll(logs);
        auditService.log(actor, "SAVE_YIELD_LOG", "YieldLog", log.getYieldId(), "Yield log saved.");
        return log;
    }

    public List<YieldLog> findVisible(User user) {
        if (user.getRole() == Role.ADMIN || user.getRole() == Role.OFFICER) {
            return yieldLogDAO.findAll();
        }
        return yieldLogDAO.findAll().stream()
                .filter(y -> y.getFarmerId().equalsIgnoreCase(user.getUserId()))
                .collect(Collectors.toList());
    }

    public double totalSeason() {
        LocalDate now = LocalDate.now();
        int quarter = (now.getMonthValue() - 1) / 3;
        return yieldLogDAO.findAll().stream().filter(y -> {
            int logQuarter = (y.getHarvestDate().getMonthValue() - 1) / 3;
            return y.getHarvestDate().getYear() == now.getYear() && logQuarter == quarter;
        }).mapToDouble(YieldLog::getActualKg).sum();
    }

    public double totalYear() {
        int year = LocalDate.now().getYear();
        return yieldLogDAO.findAll().stream().filter(y -> y.getHarvestDate().getYear() == year)
                .mapToDouble(YieldLog::getActualKg).sum();
    }
}
