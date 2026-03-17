package com.aurafarming.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class CropPlan implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String planId;
    private String farmerId;
    private String plotId;
    private String cropType;
    private Season season;
    private int recommendationScore;
    private LocalDate startDate;
    private LocalDate expectedHarvestDate;

    public CropPlan() {
    }

    public CropPlan(String planId, String farmerId, String plotId, String cropType, Season season,
            int recommendationScore,
            LocalDate startDate, LocalDate expectedHarvestDate) {
        this.planId = planId;
        this.farmerId = farmerId;
        this.plotId = plotId;
        this.cropType = cropType;
        this.season = season;
        this.recommendationScore = recommendationScore;
        this.startDate = startDate;
        this.expectedHarvestDate = expectedHarvestDate;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getPlotId() {
        return plotId;
    }

    public void setPlotId(String plotId) {
        this.plotId = plotId;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public int getRecommendationScore() {
        return recommendationScore;
    }

    public void setRecommendationScore(int recommendationScore) {
        this.recommendationScore = recommendationScore;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpectedHarvestDate() {
        return expectedHarvestDate;
    }

    public void setExpectedHarvestDate(LocalDate expectedHarvestDate) {
        this.expectedHarvestDate = expectedHarvestDate;
    }
}
