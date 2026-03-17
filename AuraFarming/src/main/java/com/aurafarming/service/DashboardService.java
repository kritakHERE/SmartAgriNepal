package com.aurafarming.service;

import com.aurafarming.dao.UserDAO;
import com.aurafarming.dao.FarmDAO;
import com.aurafarming.dao.YieldLogDAO;
import com.aurafarming.model.District;
import com.aurafarming.model.Farm;
import com.aurafarming.model.Role;
import com.aurafarming.model.YieldLog;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DashboardService {
    private final UserDAO userDAO = new UserDAO();
    private final FarmDAO farmDAO = new FarmDAO();
    private final YieldLogDAO yieldLogDAO = new YieldLogDAO();
    private final YieldLogService yieldLogService = new YieldLogService();

    public long totalFarmers() {
        return userDAO.findAll().stream().filter(u -> u.getRole() == Role.FARMER && u.isActive()).count();
    }

    public long totalOfficers() {
        return userDAO.findAll().stream().filter(u -> u.getRole() == Role.OFFICER && u.isActive()).count();
    }

    public double totalYieldSeason() {
        return yieldLogService.totalSeason();
    }

    public double totalYieldYear() {
        return yieldLogService.totalYear();
    }

    public long activeLast24Hours() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        return userDAO.findAll().stream()
                .filter(u -> u.getLastLoginAt() != null && u.getLastLoginAt().isAfter(threshold))
                .count();
    }

    public Map<String, Long> userCompositionByRole() {
        Map<String, Long> composition = new LinkedHashMap<>();
        composition.put("Officers",
                userDAO.findAll().stream().filter(u -> u.getRole() == Role.OFFICER && u.isActive()).count());
        composition.put("Farmers",
                userDAO.findAll().stream().filter(u -> u.getRole() == Role.FARMER && u.isActive()).count());
        return composition;
    }

    public Map<String, Long> usersByDistrict() {
        Map<String, Long> counts = new LinkedHashMap<>();
        for (District district : District.values()) {
            Set<String> farmerIds = farmDAO.findAll().stream()
                    .filter(f -> f.getDistrict() == district)
                    .map(Farm::getFarmerId)
                    .collect(Collectors.toSet());
            counts.put(titleCase(district.name()), (long) farmerIds.size());
        }
        return counts;
    }

    public Map<String, Double> cropHarvestByDistrict() {
        Map<String, Double> totals = new LinkedHashMap<>();
        for (District district : District.values()) {
            double total = yieldLogDAO.findAll().stream()
                    .filter(y -> y.getDistrict() == district)
                    .mapToDouble(YieldLog::getActualKg)
                    .sum();
            totals.put(titleCase(district.name()), total);
        }
        return totals;
    }

    public Map<String, Double> cropHarvestByCrop() {
        return yieldLogDAO.findAll().stream()
                .collect(Collectors.groupingBy(YieldLog::getCropType, LinkedHashMap::new,
                        Collectors.summingDouble(YieldLog::getActualKg)));
    }

    private String titleCase(String source) {
        String lower = source.toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
