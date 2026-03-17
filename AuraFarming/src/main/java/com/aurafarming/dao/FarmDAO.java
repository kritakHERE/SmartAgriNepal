package com.aurafarming.dao;

import com.aurafarming.model.Farm;
import com.aurafarming.util.Constants;

import java.util.List;

public class FarmDAO extends BaseObjectDAO<Farm> {
    public FarmDAO() {
        super(Constants.FARMS_FILE);
    }

    public List<Farm> findAll() {
        return readAllInternal();
    }

    public void saveAll(List<Farm> farms) {
        writeAllInternal(farms);
    }
}
