package com.aurafarming.service;

import com.aurafarming.dao.UserDAO;
import com.aurafarming.model.Role;

import java.time.LocalDateTime;

public class DashboardService {
    private final UserDAO userDAO = new UserDAO();
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
}
