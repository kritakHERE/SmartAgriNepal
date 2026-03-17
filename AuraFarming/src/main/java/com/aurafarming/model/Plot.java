package com.aurafarming.model;

import java.io.Serial;
import java.io.Serializable;

public class Plot implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String plotId;
    private String farmId;
    private String plotCode;
    private double area;

    public Plot() {
    }

    public Plot(String plotId, String farmId, String plotCode, double area) {
        this.plotId = plotId;
        this.farmId = farmId;
        this.plotCode = plotCode;
        this.area = area;
    }

    public String getPlotId() {
        return plotId;
    }

    public void setPlotId(String plotId) {
        this.plotId = plotId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getPlotCode() {
        return plotCode;
    }

    public void setPlotCode(String plotCode) {
        this.plotCode = plotCode;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}
