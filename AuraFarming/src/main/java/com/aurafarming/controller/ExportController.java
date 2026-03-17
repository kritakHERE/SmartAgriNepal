package com.aurafarming.controller;

import com.aurafarming.dto.ExportRequestDTO;
import com.aurafarming.service.ExportService;
import com.aurafarming.service.SessionContext;
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
    private TextField districtField;
    @FXML
    private TextField farmerField;
    @FXML
    private TextArea outputArea;

    private final ExportService exportService = new ExportService();

    @FXML
    public void initialize() {
        reportTypeCombo.setItems(FXCollections.observableArrayList("audit", "yield", "market", "crop-plan"));
        reportTypeCombo.getSelectionModel().selectFirst();
        fromDatePicker.setValue(LocalDate.now().minusDays(30));
        toDatePicker.setValue(LocalDate.now());
    }

    @FXML
    public void onExport() {
        var output = exportService.export(SessionContext.getCurrentUser(),
                new ExportRequestDTO(
                        reportTypeCombo.getValue(),
                        fromDatePicker.getValue(),
                        toDatePicker.getValue(),
                        districtField.getText(),
                        farmerField.getText(),
                        SessionContext.getCurrentUser().getRole().name()));
        outputArea.setText("Export successful: " + output.toAbsolutePath());
    }
}
