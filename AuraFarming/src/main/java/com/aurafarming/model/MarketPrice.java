package com.aurafarming.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class MarketPrice implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String priceId;
    private District district;
    private String cropType;
    private double pricePerKg;
    private LocalDate date;

    public MarketPrice() {
    }

    public MarketPrice(String priceId, District district, String cropType, double pricePerKg, LocalDate date) {
        this.priceId = priceId;
        this.district = district;
        this.cropType = cropType;
        this.pricePerKg = pricePerKg;
        this.date = date;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
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

    public double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
