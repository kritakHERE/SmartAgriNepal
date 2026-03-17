package com.aurafarming.dao;

import com.aurafarming.model.YieldLog;
import com.aurafarming.util.Constants;

import java.util.List;

public class YieldLogDAO extends BaseObjectDAO<YieldLog> {
    public YieldLogDAO() {
        super(Constants.YIELD_LOG_FILE);
    }

    public List<YieldLog> findAll() {
        return readAllInternal();
    }

    public void saveAll(List<YieldLog> yieldLogs) {
        writeAllInternal(yieldLogs);
    }
}
