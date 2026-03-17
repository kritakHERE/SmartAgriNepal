package com.aurafarming.controller;

import com.aurafarming.model.District;
import com.aurafarming.model.Plot;
import com.aurafarming.model.Role;
import com.aurafarming.service.SessionContext;
import com.aurafarming.service.FarmPlotService;
import com.aurafarming.service.YieldLogService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class YieldLogController {
    private static final String[] CROPS = { "Rice", "Wheat", "Mustard", "Maize", "Millet", "Vegetable" };

    @FXML
    private TextField farmerIdField;
    @FXML
    private ComboBox<String> plotCombo;
    @FXML
    private ComboBox<District> districtCombo;
    @FXML
    private ComboBox<String> cropCombo;
    @FXML
    private TextField estimatedField;
    @FXML
    private TextField actualField;
    @FXML
    private DatePicker harvestDatePicker;
    @FXML
    private TextArea outputArea;

    private final YieldLogService yieldLogService = new YieldLogService();
    private final FarmPlotService farmPlotService = new FarmPlotService();

    @FXML
    public void initialize() {
        districtCombo.setItems(FXCollections.observableArrayList(District.values()));
        districtCombo.getSelectionModel().selectFirst();
        cropCombo.setItems(FXCollections.observableArrayList(CROPS));
        cropCombo.getSelectionModel().selectFirst();

        var plotIds = farmPlotService.getPlotsForUser(SessionContext.getCurrentUser()).stream().map(Plot::getPlotId)
                .toList();
        plotCombo.setItems(FXCollections.observableArrayList(plotIds));
        if (!plotIds.isEmpty()) {
            plotCombo.getSelectionModel().selectFirst();
        }
        plotCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            updateDistrictFromPlot(newValue);
        });
        updateDistrictFromPlot(plotCombo.getValue());

        if (SessionContext.getCurrentUser().getRole() == Role.FARMER) {
            farmerIdField.setDisable(true);
            farmerIdField.setManaged(false);
            farmerIdField.setVisible(false);
        }

        harvestDatePicker.setValue(LocalDate.now());
        refresh();
    }

    @FXML
    public void onSave() {
        String farmerId = farmerIdField.getText().isBlank() ? SessionContext.getCurrentUser().getUserId()
                : farmerIdField.getText();
        if (plotCombo.getValue() == null || plotCombo.getValue().isBlank()) {
            outputArea.setText("Create farm and plot first, then select the plot.");
            return;
        }
        yieldLogService.save(SessionContext.getCurrentUser(), farmerId, plotCombo.getValue(), districtCombo.getValue(),
                cropCombo.getValue(),
                Double.parseDouble(estimatedField.getText()), Double.parseDouble(actualField.getText()),
                harvestDatePicker.getValue());
        refresh();
    }

    private void updateDistrictFromPlot(String plotId) {
        if (plotId == null || plotId.isBlank()) {
            return;
        }
        var selectedPlot = farmPlotService.getPlotsForUser(SessionContext.getCurrentUser()).stream()
                .filter(p -> p.getPlotId().equalsIgnoreCase(plotId)).findFirst().orElse(null);
        if (selectedPlot == null) {
            return;
        }
        var farm = farmPlotService.getFarmById(selectedPlot.getFarmId());
        if (farm != null && farm.getDistrict() != null) {
            districtCombo.getSelectionModel().select(farm.getDistrict());
        }
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
