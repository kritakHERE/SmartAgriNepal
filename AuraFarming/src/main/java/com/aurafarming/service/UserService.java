package com.aurafarming.service;

import com.aurafarming.dao.UserDAO;
import com.aurafarming.exception.ValidationException;
import com.aurafarming.model.Role;
import com.aurafarming.model.User;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class UserService {
    private final UserDAO userDAO = new UserDAO();
    private final AuditService auditService = new AuditService();

    public List<User> findByRole(Role role) {
        return userDAO.findAll().stream().filter(u -> u.getRole() == role).collect(Collectors.toList());
    }

    public List<User> search(Role role, String keyword) {
        return findByRole(role).stream().filter(u -> keyword == null || keyword.isBlank() ||
                u.getUserId().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT)) ||
                u.getEmail().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT)) ||
                u.getFullName().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }

    public void updateUser(User updated) {
        List<User> users = userDAO.findAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equalsIgnoreCase(updated.getUserId())) {
                users.set(i, updated);
                userDAO.saveAll(users);
                auditService.log(SessionContext.getCurrentUser(), "UPDATE_USER", "User", updated.getUserId(),
                        "User updated.");
                return;
            }
        }
        throw new ValidationException("User not found.");
    }

    public void deactivateUser(String userId) {
        List<User> users = userDAO.findAll();
        for (User user : users) {
            if (user.getUserId().equalsIgnoreCase(userId)) {
                user.setActive(false);
                userDAO.saveAll(users);
                auditService.log(SessionContext.getCurrentUser(), "DEACTIVATE_USER", "User", userId,
                        "User deactivated.");
                return;
            }
        }
        throw new ValidationException("User not found.");
    }
}
