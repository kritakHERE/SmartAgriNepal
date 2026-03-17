package com.aurafarming.service;

import com.aurafarming.dao.AuditLogDAO;
import com.aurafarming.model.AuditLog;
import com.aurafarming.model.Role;
import com.aurafarming.model.User;
import com.aurafarming.util.IdGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AuditService {
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    public void log(User actor, String action, String targetType, String targetId, String details) {
        Role role = actor == null ? Role.ADMIN : actor.getRole();
        String actorId = actor == null ? "SYSTEM" : actor.getUserId();
        AuditLog log = new AuditLog(
                IdGenerator.next("LOG"), actorId, role, action, targetType, targetId,
                LocalDateTime.now(), details);
        List<AuditLog> logs = auditLogDAO.findAll();
        logs.add(log);
        auditLogDAO.saveAll(logs);
    }

    public List<AuditLog> findAll() {
        return auditLogDAO.findAll();
    }

    public List<AuditLog> filter(String userKeyword, String action, LocalDate from, LocalDate to, Role viewerRole) {
        return findAll().stream().filter(log -> {
            boolean userMatch = userKeyword == null || userKeyword.isBlank() ||
                    log.getActorId().toLowerCase(Locale.ROOT).contains(userKeyword.toLowerCase(Locale.ROOT));
            boolean actionMatch = action == null || action.isBlank() || action.equalsIgnoreCase("ALL") ||
                    log.getAction().equalsIgnoreCase(action);
            boolean fromMatch = from == null || !log.getTimestamp().toLocalDate().isBefore(from);
            boolean toMatch = to == null || !log.getTimestamp().toLocalDate().isAfter(to);
            boolean roleMatch = viewerRole == Role.ADMIN || log.getAction().toLowerCase(Locale.ROOT).contains("login")
                    || log.getAction().toLowerCase(Locale.ROOT).contains("yield");
            return userMatch && actionMatch && fromMatch && toMatch && roleMatch;
        }).collect(Collectors.toList());
    }
}
