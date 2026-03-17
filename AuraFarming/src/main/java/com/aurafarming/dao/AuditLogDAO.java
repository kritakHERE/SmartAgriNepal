package com.aurafarming.dao;

import com.aurafarming.model.AuditLog;
import com.aurafarming.util.Constants;

import java.util.List;

public class AuditLogDAO extends BaseObjectDAO<AuditLog> {
    public AuditLogDAO() {
        super(Constants.AUDIT_LOG_FILE);
    }

    public List<AuditLog> findAll() {
        return readAllInternal();
    }

    public void saveAll(List<AuditLog> logs) {
        writeAllInternal(logs);
    }
}
