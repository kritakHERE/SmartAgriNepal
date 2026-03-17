package com.aurafarming.dao;

import com.aurafarming.model.CropPlan;
import com.aurafarming.util.Constants;

import java.util.List;

public class CropPlanDAO extends BaseObjectDAO<CropPlan> {
    public CropPlanDAO() {
        super(Constants.CROP_PLAN_FILE);
    }

    public List<CropPlan> findAll() {
        return readAllInternal();
    }

    public void saveAll(List<CropPlan> cropPlans) {
        writeAllInternal(cropPlans);
    }
}
