package com.aurafarming.service;

import com.aurafarming.dao.UserDAO;
import com.aurafarming.dto.LoginDTO;
import com.aurafarming.dto.RegistrationDTO;
import com.aurafarming.exception.AuthenticationException;
import com.aurafarming.exception.ValidationException;
import com.aurafarming.model.*;
import com.aurafarming.util.Constants;
import com.aurafarming.util.IdGenerator;

import java.util.ArrayList;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();
    private final AuditService auditService = new AuditService();

    public AuthService() {
        ensureAdminSeed();
    }

    public User register(RegistrationDTO dto) {
        validateRegistration(dto);
        if (userDAO.findByEmail(dto.email()).isPresent()) {
            throw new ValidationException("Email already exists.");
        }

        String userId = IdGenerator.next(dto.role() == Role.OFFICER ? "OFF" : "FRM");
        User newUser;
        if (dto.role() == Role.FARMER) {
            newUser = new Farmer(userId, dto.fullName(), dto.email(), dto.password(), dto.district(), dto.phone());
        } else if (dto.role() == Role.OFFICER) {
            Officer officer = new Officer(userId, dto.fullName(), dto.email(), dto.password());
            officer.setAssignedDistricts(new ArrayList<>());
            newUser = officer;
        } else {
            throw new ValidationException("Registration for selected role is not allowed.");
        }

        userDAO.save(newUser);
        auditService.log(SessionContext.getCurrentUser(), "REGISTER", "User", newUser.getUserId(),
                "New user registered.");
        return newUser;
    }

    public User login(LoginDTO dto) {
        User user = userDAO.findByEmail(dto.email())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password."));

        if (!user.getPassword().equals(dto.password())) {
            auditService.log(user, "LOGIN_FAIL", "User", user.getUserId(), "Invalid password.");
            throw new AuthenticationException("Invalid email or password.");
        }
        if (!user.isActive()) {
            throw new AuthenticationException("This user account is inactive.");
        }

        user.setLastLoginAt(java.time.LocalDateTime.now());
        updateUser(user);
        SessionContext.setCurrentUser(user);
        auditService.log(user, "LOGIN_SUCCESS", "User", user.getUserId(), "User logged in.");
        return user;
    }

    public void logout() {
        User current = SessionContext.getCurrentUser();
        if (current != null) {
            auditService.log(current, "LOGOUT", "User", current.getUserId(), "User logged out.");
        }
        SessionContext.clear();
    }

    private void validateRegistration(RegistrationDTO dto) {
        if (dto.fullName() == null || dto.fullName().isBlank() || dto.email() == null || dto.email().isBlank()
                || dto.password() == null || dto.password().isBlank() || dto.confirmPassword() == null
                || dto.confirmPassword().isBlank()) {
            throw new ValidationException("All fields are required.");
        }
        if (!dto.password().equals(dto.confirmPassword())) {
            throw new ValidationException("Password and confirm password must match.");
        }
        if (!dto.email().contains("@") || !dto.email().contains(".")) {
            throw new ValidationException("Invalid email format.");
        }
        if (dto.role() == Role.ADMIN) {
            throw new ValidationException("Admin cannot be registered from this screen.");
        }
        if (dto.role() == Role.FARMER && dto.district() == null) {
            throw new ValidationException("District is required for Farmer registration.");
        }
    }

    private void ensureAdminSeed() {
        if (userDAO.findByEmail(Constants.DEFAULT_ADMIN_EMAIL).isPresent()) {
            return;
        }
        Admin admin = new Admin(IdGenerator.next("ADM"), Constants.DEFAULT_ADMIN_NAME,
                Constants.DEFAULT_ADMIN_EMAIL, Constants.DEFAULT_ADMIN_PASSWORD, 10);
        userDAO.save(admin);
    }

    private void updateUser(User updatedUser) {
        java.util.List<User> users = userDAO.findAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equalsIgnoreCase(updatedUser.getUserId())) {
                users.set(i, updatedUser);
                userDAO.saveAll(users);
                return;
            }
        }
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
}
