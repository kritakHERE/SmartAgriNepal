package com.aurafarming.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class Officer extends User {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<District> assignedDistricts = new ArrayList<>();

    public Officer() {
    }

    public Officer(String userId, String fullName, String email, String password) {
        super(userId, fullName, email, password, Role.OFFICER);
    }

    public List<District> getAssignedDistricts() {
        return assignedDistricts;
    }

    public void setAssignedDistricts(List<District> assignedDistricts) {
        this.assignedDistricts = assignedDistricts;
    }
}
