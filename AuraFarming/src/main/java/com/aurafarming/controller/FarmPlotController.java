package com.aurafarming.controller;

import com.aurafarming.model.*;
import com.aurafarming.service.FarmPlotService;
import com.aurafarming.service.SessionContext;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class FarmPlotController {
    private static final String[] FARM_UNITS = { "ROPANI", "HECTARE", "ACRE" };
    private static final String[] FARM_TAGS = { "Irrigated", "Organic", "Rainfed", "Commercial", "Demo" };

    @FXML
    private TextField farmerIdField;
    @FXML
    private ComboBox<District> districtCombo;
    @FXML
    private ComboBox<String> unitCombo;
    @FXML
    private ComboBox<String> farmTagCombo;
    @FXML
    private TextField farmAreaField;
    @FXML
    private ComboBox<String> farmCombo;
    @FXML
    private ComboBox<String> plotCombo;
    @FXML
    private TextField plotAreaField;
    @FXML
    private TextArea outputArea;

    private final FarmPlotService farmPlotService = new FarmPlotService();

    @FXML
    public void initialize() {
        districtCombo.setItems(FXCollections.observableArrayList(District.values()));
        districtCombo.getSelectionModel().selectFirst();
        unitCombo.setItems(FXCollections.observableArrayList(FARM_UNITS));
        unitCombo.getSelectionModel().selectFirst();
        farmTagCombo.setItems(FXCollections.observableArrayList(FARM_TAGS));
        farmTagCombo.getSelectionModel().selectFirst();
        farmTagCombo.setEditable(true);

        farmCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            updatePlotOptions();
        });

        if (SessionContext.getCurrentUser().getRole() == Role.FARMER) {
            farmerIdField.setDisable(true);
            farmerIdField.setManaged(false);
            farmerIdField.setVisible(false);
        }
        refresh();
    }

    @FXML
    public void onCreateFarm() {
        User current = SessionContext.getCurrentUser();
        String farmerId = farmerIdField.getText().isBlank() ? current.getUserId() : farmerIdField.getText();
        farmPlotService.createFarm(current, farmerId, districtCombo.getValue(), farmTagCombo.getEditor().getText(),
                unitCombo.getValue(),
                Double.parseDouble(farmAreaField.getText()));
        refresh();
    }

    @FXML
    public void onCreatePlot() {
        farmPlotService.createPlot(SessionContext.getCurrentUser(), farmCombo.getValue(),
                Double.parseDouble(plotAreaField.getText()));
        refresh();
    }

    @FXML
    public void onDeleteFarm() {
        farmPlotService.deleteFarm(SessionContext.getCurrentUser(), farmCombo.getValue());
        refresh();
    }

    private void updatePlotOptions() {
        String selectedFarm = farmCombo.getValue();
        if (selectedFarm == null || selectedFarm.isBlank()) {
            plotCombo.setItems(FXCollections.observableArrayList());
            plotCombo.getSelectionModel().clearSelection();
            plotCombo.setDisable(true);
            return;
        }
        List<String> plotIds = farmPlotService.getPlotsForFarm(selectedFarm).stream().map(Plot::getPlotId).toList();
        plotCombo.setItems(FXCollections.observableArrayList(plotIds));
        plotCombo.setDisable(plotIds.isEmpty());
        if (!plotIds.isEmpty()) {
            plotCombo.getSelectionModel().selectFirst();
        }
    }

    private void refresh() {
        List<Farm> farms = farmPlotService.getFarmsForUser(SessionContext.getCurrentUser());
        List<String> farmIds = farms.stream().map(Farm::getFarmId).toList();
        farmCombo.setItems(FXCollections.observableArrayList(farmIds));
        if (!farmIds.isEmpty() && (farmCombo.getValue() == null || !farmIds.contains(farmCombo.getValue()))) {
            farmCombo.getSelectionModel().selectFirst();
        }
        updatePlotOptions();

        StringBuilder sb = new StringBuilder();
        if (farms.isEmpty()) {
            sb.append("No farms yet. Create your first farm.\n");
        }
        for (Farm farm : farms) {
            sb.append("Farm ").append(farm.getFarmId()).append(" | farmer=").append(farm.getFarmerId())
                    .append(" | district=").append(farm.getDistrict())
                    .append(" | tag=")
                    .append(farm.getFarmTag() == null || farm.getFarmTag().isBlank() ? "-" : farm.getFarmTag())
                    .append(" | area=").append(farm.getTotalArea()).append(" ").append(farm.getMeasurementUnit())
                    .append("\n");
            for (Plot plot : farmPlotService.getPlotsForFarm(farm.getFarmId())) {
                sb.append("  -> Plot ").append(plot.getPlotCode()).append(" (id=").append(plot.getPlotId())
                        .append(", area=").append(plot.getArea()).append(")\n");
            }
        }
        outputArea.setText(sb.toString());
    }
}
