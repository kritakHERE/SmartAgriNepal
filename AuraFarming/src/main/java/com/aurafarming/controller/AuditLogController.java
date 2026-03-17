package com.aurafarming.controller;

import com.aurafarming.model.Role;
import com.aurafarming.service.AuditService;
import com.aurafarming.service.SessionContext;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class AuditLogController {
    @FXML
    private TextField userKeywordField;
    @FXML
    private TextField actionField;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private TextArea outputArea;

    private final AuditService auditService = new AuditService();

    @FXML
    public void initialize() {
        fromDatePicker.setValue(LocalDate.now().minusDays(7));
        toDatePicker.setValue(LocalDate.now());
        onFilter();
    }

    @FXML
    public void onFilter() {
        Role role = SessionContext.getCurrentUser().getRole();
        var logs = auditService.filter(userKeywordField.getText(), actionField.getText(),
                fromDatePicker.getValue(), toDatePicker.getValue(), role);
        StringBuilder sb = new StringBuilder();
        if (logs.isEmpty()) {
            sb.append("No audit records found.");
        }
        for (var log : logs) {
            sb.append(log.getTimestamp()).append(" | ").append(log.getActorId()).append(" | ")
                    .append(log.getAction()).append(" | ").append(log.getTargetType())
                    .append(" | ").append(log.getDetails()).append("\n");
        }
        outputArea.setText(sb.toString());
    }
}
