package com.aurafarming.controller;

import com.aurafarming.model.District;
import com.aurafarming.service.SessionContext;
import com.aurafarming.service.YieldLogService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class YieldLogController {
    @FXML
    private TextField farmerIdField;
    @FXML
    private TextField plotIdField;
    @FXML
    private ComboBox<District> districtCombo;
    @FXML
    private TextField cropField;
    @FXML
    private TextField estimatedField;
    @FXML
    private TextField actualField;
    @FXML
    private DatePicker harvestDatePicker;
    @FXML
    private TextArea outputArea;

    private final YieldLogService yieldLogService = new YieldLogService();

    @FXML
    public void initialize() {
        districtCombo.setItems(FXCollections.observableArrayList(District.values()));
        districtCombo.getSelectionModel().selectFirst();
        harvestDatePicker.setValue(LocalDate.now());
        refresh();
    }

    @FXML
    public void onSave() {
        String farmerId = farmerIdField.getText().isBlank() ? SessionContext.getCurrentUser().getUserId()
                : farmerIdField.getText();
        yieldLogService.save(SessionContext.getCurrentUser(), farmerId, plotIdField.getText(), districtCombo.getValue(),
                cropField.getText(),
                Double.parseDouble(estimatedField.getText()), Double.parseDouble(actualField.getText()),
                harvestDatePicker.getValue());
        refresh();
    }

    private void refresh() {
        StringBuilder sb = new StringBuilder();
        var logs = yieldLogService.findVisible(SessionContext.getCurrentUser());
        if (logs.isEmpty()) {
            sb.append("No yield logs yet.");
        }
        for (var y : logs) {
            sb.append(y.getHarvestDate()).append(" | ").append(y.getFarmerId()).append(" | ")
                    .append(y.getCropType()).append(" | estimated ").append(y.getEstimatedKg())
                    .append(" | actual ").append(y.getActualKg()).append("\n");
        }
        outputArea.setText(sb.toString());
    }
}
