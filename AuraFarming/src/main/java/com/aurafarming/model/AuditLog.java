package com.aurafarming.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class AuditLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String logId;
    private String actorId;
    private Role actorRole;
    private String action;
    private String targetType;
    private String targetId;
    private LocalDateTime timestamp;
    private String details;

    public AuditLog() {
    }

    public AuditLog(String logId, String actorId, Role actorRole, String action, String targetType, String targetId,
            LocalDateTime timestamp, String details) {
        this.logId = logId;
        this.actorId = actorId;
        this.actorRole = actorRole;
        this.action = action;
        this.targetType = targetType;
        this.targetId = targetId;
        this.timestamp = timestamp;
        this.details = details;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public Role getActorRole() {
        return actorRole;
    }

    public void setActorRole(Role actorRole) {
        this.actorRole = actorRole;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
