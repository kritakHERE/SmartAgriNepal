package com.aurafarming.controller;

import com.aurafarming.model.Role;
import com.aurafarming.model.User;
import com.aurafarming.service.SessionContext;
import com.aurafarming.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

public class FarmerController {
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private TextArea outputArea;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        if (SessionContext.getCurrentUser().getRole() != Role.ADMIN) {
            searchField.setManaged(false);
            searchField.setVisible(false);
            searchButton.setManaged(false);
            searchButton.setVisible(false);
            render(userService.search(Role.FARMER, SessionContext.getCurrentUser().getUserId()));
            return;
        }
        render(userService.findByRole(Role.FARMER));
    }

    @FXML
    public void onSearch() {
        render(userService.search(Role.FARMER, searchField.getText()));
    }

    @FXML
    public void onDeactivate() {
        User current = SessionContext.getCurrentUser();
        List<User> list = current.getRole() == Role.ADMIN
                ? userService.search(Role.FARMER, searchField.getText())
                : userService.search(Role.FARMER, current.getUserId());
        if (list.isEmpty()) {
            return;
        }
        User target = list.get(0);
        if (current.getRole() == Role.FARMER && !current.getUserId().equalsIgnoreCase(target.getUserId())) {
            outputArea.setText("Farmer can deactivate only own profile.");
            return;
        }
        userService.deactivateUser(target.getUserId());
        render(current.getRole() == Role.ADMIN
                ? userService.search(Role.FARMER, searchField.getText())
                : userService.search(Role.FARMER, current.getUserId()));
    }

    @FXML
    public void onReactivate() {
        User current = SessionContext.getCurrentUser();
        List<User> list = current.getRole() == Role.ADMIN
                ? userService.search(Role.FARMER, searchField.getText())
                : userService.search(Role.FARMER, current.getUserId());
        if (list.isEmpty()) {
            return;
        }
        User target = list.get(0);
        if (current.getRole() == Role.FARMER && !current.getUserId().equalsIgnoreCase(target.getUserId())) {
            outputArea.setText("Farmer can reactivate only own profile.");
            return;
        }
        userService.reactivateUser(target.getUserId());
        render(current.getRole() == Role.ADMIN
                ? userService.search(Role.FARMER, searchField.getText())
                : userService.search(Role.FARMER, current.getUserId()));
    }

    private void render(List<User> users) {
        if (users.isEmpty()) {
            outputArea.setText("No farmer records.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append(user.getUserId()).append(" | ")
                    .append(user.getFullName()).append(" | ")
                    .append(user.getEmail()).append(" | active=")
                    .append(user.isActive()).append("\n");
        }
        outputArea.setText(sb.toString());
    }
}
