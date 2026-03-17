package com.aurafarming.controller;

import com.aurafarming.dto.RegistrationDTO;
import com.aurafarming.model.District;
import com.aurafarming.model.Role;
import com.aurafarming.service.AuthService;
import com.aurafarming.util.AlertUtil;
import com.aurafarming.util.SceneRouter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegistrationController {
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private ComboBox<Role> roleCombo;
    @FXML
    private ComboBox<District> districtCombo;
    @FXML
    private TextField phoneField;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        roleCombo.setItems(FXCollections.observableArrayList(Role.FARMER, Role.OFFICER));
        roleCombo.getSelectionModel().select(Role.FARMER);
        districtCombo.setItems(FXCollections.observableArrayList(District.values()));
        districtCombo.getSelectionModel().selectFirst();
    }

    @FXML
    public void onRegister() {
        try {
            authService.register(new RegistrationDTO(
                    fullNameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    confirmPasswordField.getText(),
                    roleCombo.getValue(),
                    districtCombo.getValue(),
                    phoneField.getText()));
            AlertUtil.info("Registration", "Registration successful.");
            onLoginInstead();
        } catch (Exception ex) {
            AlertUtil.error("Registration Failed", ex.getMessage());
        }
    }

    @FXML
    public void onLoginInstead() {
        SceneRouter.goTo("/com/aurafarming/view/login.fxml", "AuraFarming - Login");
    }
}
