package com.aurafarming.controller;

import com.aurafarming.model.*;
import com.aurafarming.service.FarmPlotService;
import com.aurafarming.service.SessionContext;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class FarmPlotController {
    @FXML
    private TextField farmerIdField;
    @FXML
    private ComboBox<District> districtCombo;
    @FXML
    private TextField unitField;
    @FXML
    private TextField farmAreaField;
    @FXML
    private TextField farmIdForPlotField;
    @FXML
    private TextField plotAreaField;
    @FXML
    private TextArea outputArea;

    private final FarmPlotService farmPlotService = new FarmPlotService();

    @FXML
    public void initialize() {
        districtCombo.setItems(FXCollections.observableArrayList(District.values()));
        districtCombo.getSelectionModel().selectFirst();
        refresh();
    }

    @FXML
    public void onCreateFarm() {
        User current = SessionContext.getCurrentUser();
        String farmerId = farmerIdField.getText().isBlank() ? current.getUserId() : farmerIdField.getText();
        farmPlotService.createFarm(current, farmerId, districtCombo.getValue(), unitField.getText(),
                Double.parseDouble(farmAreaField.getText()));
        refresh();
    }

    @FXML
    public void onCreatePlot() {
        farmPlotService.createPlot(SessionContext.getCurrentUser(), farmIdForPlotField.getText(),
                Double.parseDouble(plotAreaField.getText()));
        refresh();
    }

    @FXML
    public void onDeleteFarm() {
        farmPlotService.deleteFarm(SessionContext.getCurrentUser(), farmIdForPlotField.getText());
        refresh();
    }

    private void refresh() {
        List<Farm> farms = farmPlotService.getFarmsForUser(SessionContext.getCurrentUser());
        StringBuilder sb = new StringBuilder();
        if (farms.isEmpty()) {
            sb.append("No farms yet. Create your first farm.\n");
        }
        for (Farm farm : farms) {
            sb.append("Farm ").append(farm.getFarmId()).append(" | farmer=").append(farm.getFarmerId())
                    .append(" | district=").append(farm.getDistrict())
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
