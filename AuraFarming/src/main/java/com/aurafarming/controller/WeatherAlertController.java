package com.aurafarming.controller;

import com.aurafarming.model.*;
import com.aurafarming.service.SessionContext;
import com.aurafarming.service.WeatherAlertService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class WeatherAlertController {
    @FXML
    private ComboBox<District> districtCombo;
    @FXML
    private ComboBox<WeatherCondition> conditionCombo;
    @FXML
    private ComboBox<Severity> severityCombo;
    @FXML
    private TextField durationField;
    @FXML
    private TextField probabilityField;
    @FXML
    private TextArea outputArea;
    @FXML
    private Button saveButton;

    private final WeatherAlertService weatherAlertService = new WeatherAlertService();

    @FXML
    public void initialize() {
        districtCombo.setItems(FXCollections.observableArrayList(District.values()));
        conditionCombo.setItems(FXCollections.observableArrayList(WeatherCondition.values()));
        severityCombo.setItems(FXCollections.observableArrayList(Severity.values()));
        districtCombo.getSelectionModel().selectFirst();
        conditionCombo.getSelectionModel().selectFirst();
        severityCombo.getSelectionModel().selectFirst();
        if (SessionContext.getCurrentUser().getRole() == Role.FARMER) {
            saveButton.setDisable(true);
        }
        refresh();
    }

    @FXML
    public void onSave() {
        weatherAlertService.save(SessionContext.getCurrentUser(), districtCombo.getValue(), conditionCombo.getValue(),
                severityCombo.getValue(), Integer.parseInt(durationField.getText()),
                Integer.parseInt(probabilityField.getText()));
        refresh();
    }

    private void refresh() {
        StringBuilder sb = new StringBuilder();
        var all = weatherAlertService.findAll();
        if (all.isEmpty()) {
            sb.append("No weather alerts.");
        }
        for (var a : all) {
            sb.append(a.getCreatedDate()).append(" | ").append(a.getDistrict()).append(" | ")
                    .append(a.getCondition()).append(" | ").append(a.getSeverity())
                    .append(" | ").append(a.getProbabilityPercent()).append("%\n");
        }
        outputArea.setText(sb.toString());
    }
}
