package com.aurafarming.controller;

import com.aurafarming.model.Season;
import com.aurafarming.service.CropPlanService;
import com.aurafarming.service.SessionContext;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class CropPlanController {
    @FXML
    private TextField farmerIdField;
    @FXML
    private TextField plotIdField;
    @FXML
    private TextField cropTypeField;
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
    private int score = 0;

    @FXML
    public void initialize() {
        seasonCombo.setItems(FXCollections.observableArrayList(Season.values()));
        seasonCombo.getSelectionModel().selectFirst();
        startDatePicker.setValue(LocalDate.now());
        harvestDatePicker.setValue(LocalDate.now().plusDays(90));
    }

    @FXML
    public void onRecommend() {
        score = cropPlanService.recommendationScore(cropTypeField.getText(), seasonCombo.getValue());
        String badge = score >= 80 ? "Good" : score >= 60 ? "Moderate" : "Poor";
        recommendationLabel.setText("Score: " + score + " (" + badge + ")");
    }

    @FXML
    public void onSave() {
        String farmerId = farmerIdField.getText().isBlank() ? SessionContext.getCurrentUser().getUserId()
                : farmerIdField.getText();
        if (score == 0) {
            onRecommend();
        }
        cropPlanService.savePlan(SessionContext.getCurrentUser(), farmerId, plotIdField.getText(),
                cropTypeField.getText(),
                seasonCombo.getValue(), startDatePicker.getValue(), harvestDatePicker.getValue(), score);
        outputArea.setText("Crop plan saved. Total plans: " + cropPlanService.findAll().size());
    }
}
