package com.aurafarming.controller;

import com.aurafarming.dto.ExportRequestDTO;
import com.aurafarming.model.District;
import com.aurafarming.model.Role;
import com.aurafarming.service.ExportService;
import com.aurafarming.service.SessionContext;
import com.aurafarming.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ExportController {
    @FXML
    private ComboBox<String> reportTypeCombo;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private ComboBox<District> districtCombo;
    @FXML
    private TextField farmerField;
    @FXML
    private TextArea outputArea;

    private final ExportService exportService = new ExportService();

    @FXML
    public void initialize() {
        if (SessionContext.getCurrentUser().getRole() == Role.FARMER) {
            reportTypeCombo.setItems(FXCollections.observableArrayList("plot", "crop-plan"));
            farmerField.setText(SessionContext.getCurrentUser().getUserId());
            farmerField.setDisable(true);
            farmerField.setEditable(false);
        } else {
            reportTypeCombo
                    .setItems(FXCollections.observableArrayList("audit", "yield", "market", "crop-plan", "plot"));
        }
        reportTypeCombo.getSelectionModel().selectFirst();
        districtCombo.setItems(FXCollections.observableArrayList(District.values()));
        districtCombo.getSelectionModel().selectFirst();
        fromDatePicker.setValue(LocalDate.now().minusDays(30));
        toDatePicker.setValue(LocalDate.now());
    }

    @FXML
    public void onPreview() {
        try {
            var lines = exportService.preview(SessionContext.getCurrentUser(), buildRequest(), 25);
            outputArea.setText(String.join("\n", lines));
        } catch (Exception ex) {
            AlertUtil.error("Preview Failed", ex.getMessage());
        }
    }

    @FXML
    public void onExport() {
        try {
            var output = exportService.export(SessionContext.getCurrentUser(), buildRequest());
            outputArea.setText(outputArea.getText() + "\n\nExport successful: " + output.toAbsolutePath());
        } catch (Exception ex) {
            AlertUtil.error("Export Failed", ex.getMessage());
        }
    }

    private ExportRequestDTO buildRequest() {
        String district = districtCombo.getValue() == null ? "" : districtCombo.getValue().name();
        return new ExportRequestDTO(
                reportTypeCombo.getValue(),
                fromDatePicker.getValue(),
                toDatePicker.getValue(),
                district,
                farmerField.getText(),
                SessionContext.getCurrentUser().getRole().name());
    }
}
