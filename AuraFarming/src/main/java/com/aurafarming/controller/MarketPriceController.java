package com.aurafarming.controller;

import com.aurafarming.model.District;
import com.aurafarming.model.Role;
import com.aurafarming.model.User;
import com.aurafarming.service.MarketPriceService;
import com.aurafarming.service.SessionContext;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class MarketPriceController {
    private static final String[] CROPS = { "Rice", "Wheat", "Mustard", "Maize", "Millet", "Vegetable" };

    @FXML
    private ComboBox<District> districtCombo;
    @FXML
    private ComboBox<String> cropCombo;
    @FXML
    private TextField priceField;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private Label trendLabel;
    @FXML
    private TextArea outputArea;
    @FXML
    private Button saveButton;

    private final MarketPriceService marketPriceService = new MarketPriceService();

    @FXML
    public void initialize() {
        districtCombo.setItems(FXCollections.observableArrayList(District.values()));
        districtCombo.getSelectionModel().selectFirst();
        cropCombo.setItems(FXCollections.observableArrayList(CROPS));
        cropCombo.getSelectionModel().selectFirst();
        fromDatePicker.setValue(LocalDate.now().minusDays(30));
        toDatePicker.setValue(LocalDate.now());
        User current = SessionContext.getCurrentUser();
        if (current.getRole() == Role.FARMER) {
            saveButton.setDisable(true);
        }
        onHistory();
    }

    @FXML
    public void onSave() {
        marketPriceService.save(SessionContext.getCurrentUser(), districtCombo.getValue(), cropCombo.getValue(),
                Double.parseDouble(priceField.getText()));
        trendLabel.setText(marketPriceService.trendHint(districtCombo.getValue(), cropCombo.getValue()));
        onHistory();
    }

    @FXML
    public void onHistory() {
        StringBuilder sb = new StringBuilder();
        var list = marketPriceService.findHistory(fromDatePicker.getValue(), toDatePicker.getValue(),
                cropCombo.getValue());
        if (list.isEmpty()) {
            sb.append("No market history found.");
        }
        for (var p : list) {
            sb.append(p.getDate()).append(" | ").append(p.getDistrict()).append(" | ")
                    .append(p.getCropType()).append(" | Rs ").append(p.getPricePerKg()).append("\n");
        }
        outputArea.setText(sb.toString());
        trendLabel.setText(marketPriceService.trendHint(districtCombo.getValue(), cropCombo.getValue()));
    }
}
