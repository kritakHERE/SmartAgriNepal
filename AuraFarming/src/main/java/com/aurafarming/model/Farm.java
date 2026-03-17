package com.aurafarming.model;

import java.io.Serial;
import java.io.Serializable;

public class Farm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String farmId;
    private String farmerId;
    private District district;
    private String measurementUnit;
    private double totalArea;

    public Farm() {
    }

    public Farm(String farmId, String farmerId, District district, String measurementUnit, double totalArea) {
        this.farmId = farmId;
        this.farmerId = farmerId;
        this.district = district;
        this.measurementUnit = measurementUnit;
        this.totalArea = totalArea;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(double totalArea) {
        this.totalArea = totalArea;
    }
}
