package com.aurafarming.controller;

import com.aurafarming.model.Role;
import com.aurafarming.model.User;
import com.aurafarming.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

public class OfficerController {
    @FXML
    private TextField searchField;
    @FXML
    private TextArea outputArea;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        render(userService.findByRole(Role.OFFICER));
    }

    @FXML
    public void onSearch() {
        render(userService.search(Role.OFFICER, searchField.getText()));
    }

    @FXML
    public void onDeactivate() {
        String keyword = searchField.getText();
        List<User> list = userService.search(Role.OFFICER, keyword);
        if (!list.isEmpty()) {
            userService.deactivateUser(list.get(0).getUserId());
        }
        render(userService.search(Role.OFFICER, searchField.getText()));
    }

    private void render(List<User> users) {
        if (users.isEmpty()) {
            outputArea.setText("No officer records.");
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
