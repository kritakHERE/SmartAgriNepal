package com.aurafarming.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class YieldLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String yieldId;
    private String farmerId;
    private String plotId;
    private District district;
    private String cropType;
    private double estimatedKg;
    private double actualKg;
    private LocalDate harvestDate;

    public YieldLog() {
    }

    public YieldLog(String yieldId, String farmerId, String plotId, District district, String cropType,
            double estimatedKg, double actualKg, LocalDate harvestDate) {
        this.yieldId = yieldId;
        this.farmerId = farmerId;
        this.plotId = plotId;
        this.district = district;
        this.cropType = cropType;
        this.estimatedKg = estimatedKg;
        this.actualKg = actualKg;
        this.harvestDate = harvestDate;
    }

    public String getYieldId() {
        return yieldId;
    }

    public void setYieldId(String yieldId) {
        this.yieldId = yieldId;
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

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public double getEstimatedKg() {
        return estimatedKg;
    }

    public void setEstimatedKg(double estimatedKg) {
        this.estimatedKg = estimatedKg;
    }

    public double getActualKg() {
        return actualKg;
    }

    public void setActualKg(double actualKg) {
        this.actualKg = actualKg;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(LocalDate harvestDate) {
        this.harvestDate = harvestDate;
    }
}
