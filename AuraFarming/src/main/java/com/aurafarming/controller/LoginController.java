package com.aurafarming.controller;

import com.aurafarming.dto.LoginDTO;
import com.aurafarming.model.User;
import com.aurafarming.service.AuthService;
import com.aurafarming.util.AlertUtil;
import com.aurafarming.util.SceneRouter;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private final AuthService authService = new AuthService();

    @FXML
    public void onLogin() {
        try {
            User user = authService.login(new LoginDTO(emailField.getText(), passwordField.getText()));
            SceneRouter.goTo("/com/aurafarming/view/dashboard.fxml",
                    "AuraFarming - Dashboard (" + user.getRole() + ")");
        } catch (Exception ex) {
            AlertUtil.error("Login Failed", ex.getMessage());
        }
    }

    @FXML
    public void onGoRegister() {
        SceneRouter.goTo("/com/aurafarming/view/registration.fxml", "AuraFarming - Registration");
    }
}
