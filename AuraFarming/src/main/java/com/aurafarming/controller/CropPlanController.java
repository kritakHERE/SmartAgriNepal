package com.aurafarming.controller;

import com.aurafarming.model.Season;
import com.aurafarming.model.Role;
import com.aurafarming.service.FarmPlotService;
import com.aurafarming.service.CropPlanService;
import com.aurafarming.service.SessionContext;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class CropPlanController {
    private static final String[] CROPS = { "Rice", "Wheat", "Mustard", "Maize", "Millet", "Vegetable" };

    @FXML
    private TextField farmerIdField;
    @FXML
    private ComboBox<String> plotCombo;
    @FXML
    private ComboBox<String> cropCombo;
    @FXML
    private ComboBox<Season> seasonCombo;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker harvestDatePicker;
    @FXML
    private Label recommendationLabel;
    @FXML
    private TextArea outputArea;

    private final CropPlanService cropPlanService = new CropPlanService();
    private final FarmPlotService farmPlotService = new FarmPlotService();
    private int score = 0;

    @FXML
    public void initialize() {
        seasonCombo.setItems(FXCollections.observableArrayList(Season.values()));
        seasonCombo.getSelectionModel().selectFirst();
        cropCombo.setItems(FXCollections.observableArrayList(CROPS));
        cropCombo.getSelectionModel().selectFirst();
        var plotIds = farmPlotService.getPlotsForUser(SessionContext.getCurrentUser()).stream()
                .map(p -> p.getPlotId()).toList();
        plotCombo.setItems(FXCollections.observableArrayList(plotIds));
        if (!plotIds.isEmpty()) {
            plotCombo.getSelectionModel().selectFirst();
        }

        if (SessionContext.getCurrentUser().getRole() == Role.FARMER) {
            farmerIdField.setDisable(true);
            farmerIdField.setManaged(false);
            farmerIdField.setVisible(false);
        }

        startDatePicker.setValue(LocalDate.now());
        harvestDatePicker.setValue(LocalDate.now().plusDays(90));
    }

    @FXML
    public void onRecommend() {
        score = cropPlanService.recommendationScore(cropCombo.getValue(), seasonCombo.getValue());
        String badge = score >= 80 ? "Good" : score >= 60 ? "Moderate" : "Poor";
        recommendationLabel.setText("Score: " + score + " (" + badge + ")");
    }

    @FXML
    public void onSave() {
        String farmerId = farmerIdField.getText().isBlank() ? SessionContext.getCurrentUser().getUserId()
                : farmerIdField.getText();
        if (plotCombo.getValue() == null || plotCombo.getValue().isBlank()) {
            outputArea.setText("Create farm and plot first, then select the plot.");
            return;
        }
        if (score == 0) {
            onRecommend();
        }
        cropPlanService.savePlan(SessionContext.getCurrentUser(), farmerId, plotCombo.getValue(),
                cropCombo.getValue(),
                seasonCombo.getValue(), startDatePicker.getValue(), harvestDatePicker.getValue(), score);
        outputArea.setText("Crop plan saved. Total plans: " + cropPlanService.findAll().size());
    }
}
