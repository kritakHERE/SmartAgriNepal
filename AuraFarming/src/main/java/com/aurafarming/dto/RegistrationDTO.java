package com.aurafarming.dto;

import com.aurafarming.model.District;
import com.aurafarming.model.Role;

public record RegistrationDTO(String fullName, String email, String password, String confirmPassword, Role role,
        District district, String phone) {
}
