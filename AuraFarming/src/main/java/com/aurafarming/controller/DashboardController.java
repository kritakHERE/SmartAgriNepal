package com.aurafarming.controller;

import com.aurafarming.model.Role;
import com.aurafarming.model.User;
import com.aurafarming.service.AuthService;
import com.aurafarming.service.DashboardService;
import com.aurafarming.service.SessionContext;
import com.aurafarming.util.AlertUtil;
import com.aurafarming.util.SceneRouter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardController {
    @FXML
    private Label totalFarmersLabel;
    @FXML
    private Label totalOfficersLabel;
    @FXML
    private Label seasonYieldLabel;
    @FXML
    private Label yearYieldLabel;
    @FXML
    private Label activeUsersLabel;
    @FXML
    private VBox moduleContainer;

    @FXML
    private Button officersTab;
    @FXML
    private Button farmersTab;
    @FXML
    private Button farmPlotsTab;
    @FXML
    private Button cropPlanTab;
    @FXML
    private Button marketTab;
    @FXML
    private Button weatherTab;
    @FXML
    private Button yieldTab;
    @FXML
    private Button auditTab;
    @FXML
    private Button exportTab;

    private final DashboardService dashboardService = new DashboardService();
    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        User user = SessionContext.getCurrentUser();
        if (user == null) {
            SceneRouter.goTo("/com/aurafarming/view/login.fxml", "AuraFarming - Login");
            return;
        }
        refreshMetrics();
        applyRoleVisibility(user.getRole());
        onFarmersTab();
    }

    private void applyRoleVisibility(Role role) {
        if (role == Role.FARMER) {
            officersTab.setVisible(false);
            farmersTab.setVisible(false);
            auditTab.setVisible(false);
        }
        if (role == Role.OFFICER) {
            officersTab.setVisible(false);
        }
    }

    private void refreshMetrics() {
        totalFarmersLabel.setText(String.valueOf(dashboardService.totalFarmers()));
        totalOfficersLabel.setText(String.valueOf(dashboardService.totalOfficers()));
        seasonYieldLabel.setText(String.format("%.2f kg", dashboardService.totalYieldSeason()));
        yearYieldLabel.setText(String.format("%.2f kg", dashboardService.totalYieldYear()));
        activeUsersLabel.setText(String.valueOf(dashboardService.activeLast24Hours()));
    }

    private void loadModule(String fxmlPath) {
        try {
            Parent module = FXMLLoader.load(getClass().getResource(fxmlPath));
            moduleContainer.getChildren().setAll(module);
            refreshMetrics();
        } catch (Exception ex) {
            AlertUtil.error("Module Load Error", ex.getMessage());
        }
    }

    @FXML
    public void onOfficersTab() {
        loadModule("/com/aurafarming/view/officers.fxml");
    }

    @FXML
    public void onFarmersTab() {
        loadModule("/com/aurafarming/view/farmers.fxml");
    }

    @FXML
    public void onFarmPlotsTab() {
        loadModule("/com/aurafarming/view/farm-plots.fxml");
    }

    @FXML
    public void onCropPlanTab() {
        loadModule("/com/aurafarming/view/crop-plan.fxml");
    }

    @FXML
    public void onMarketTab() {
        loadModule("/com/aurafarming/view/market-price.fxml");
    }

    @FXML
    public void onWeatherTab() {
        loadModule("/com/aurafarming/view/weather-alert.fxml");
    }

    @FXML
    public void onYieldTab() {
        loadModule("/com/aurafarming/view/yield-log.fxml");
    }

    @FXML
    public void onAuditTab() {
        loadModule("/com/aurafarming/view/audit-log.fxml");
    }

    @FXML
    public void onExportTab() {
        loadModule("/com/aurafarming/view/export.fxml");
    }

    @FXML
    public void onLogout() {
        authService.logout();
        SceneRouter.goTo("/com/aurafarming/view/login.fxml", "AuraFarming - Login");
    }
}
