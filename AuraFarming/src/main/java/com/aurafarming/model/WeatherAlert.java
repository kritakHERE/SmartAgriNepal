package com.aurafarming.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class WeatherAlert implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String alertId;
    private District district;
    private WeatherCondition condition;
    private Severity severity;
    private int durationDays;
    private int probabilityPercent;
    private LocalDate createdDate;

    public WeatherAlert() {
    }

    public WeatherAlert(String alertId, District district, WeatherCondition condition, Severity severity,
            int durationDays,
            int probabilityPercent, LocalDate createdDate) {
        this.alertId = alertId;
        this.district = district;
        this.condition = condition;
        this.severity = severity;
        this.durationDays = durationDays;
        this.probabilityPercent = probabilityPercent;
        this.createdDate = createdDate;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public WeatherCondition getCondition() {
        return condition;
    }

    public void setCondition(WeatherCondition condition) {
        this.condition = condition;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public int getProbabilityPercent() {
        return probabilityPercent;
    }

    public void setProbabilityPercent(int probabilityPercent) {
        this.probabilityPercent = probabilityPercent;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
