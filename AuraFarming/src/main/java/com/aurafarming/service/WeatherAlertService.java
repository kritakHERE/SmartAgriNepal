package com.aurafarming.service;

import com.aurafarming.dao.WeatherAlertDAO;
import com.aurafarming.model.*;
import com.aurafarming.util.IdGenerator;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherAlertService {
    private final WeatherAlertDAO weatherAlertDAO = new WeatherAlertDAO();
    private final AuditService auditService = new AuditService();

    public WeatherAlert save(User actor, District district, WeatherCondition condition, Severity severity,
            int durationDays, int probabilityPercent) {
        WeatherAlert alert = new WeatherAlert(IdGenerator.next("ALT"), district, condition, severity, durationDays,
                probabilityPercent, LocalDate.now());
        List<WeatherAlert> all = weatherAlertDAO.findAll();
        all.add(alert);
        weatherAlertDAO.saveAll(all);
        auditService.log(actor, "SAVE_WEATHER_ALERT", "WeatherAlert", alert.getAlertId(), "Weather alert saved.");
        return alert;
    }

    public List<WeatherAlert> findAll() {
        return weatherAlertDAO.findAll().stream()
                .sorted(Comparator.comparing(WeatherAlert::getCreatedDate).reversed())
                .collect(Collectors.toList());
    }
}
