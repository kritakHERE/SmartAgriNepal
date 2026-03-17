package com.aurafarming.dao;

import com.aurafarming.model.Plot;
import com.aurafarming.util.Constants;

import java.util.List;

public class PlotDAO extends BaseObjectDAO<Plot> {
    public PlotDAO() {
        super(Constants.PLOTS_FILE);
    }

    public List<Plot> findAll() {
        return readAllInternal();
    }

    public void saveAll(List<Plot> plots) {
        writeAllInternal(plots);
    }
}
