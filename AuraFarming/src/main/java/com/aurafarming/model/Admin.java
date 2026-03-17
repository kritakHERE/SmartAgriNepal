package com.aurafarming.model;

import java.io.Serial;

public class Admin extends User {
    @Serial
    private static final long serialVersionUID = 1L;

    private int privilegeLevel;

    public Admin() {
    }

    public Admin(String userId, String fullName, String email, String password, int privilegeLevel) {
        super(userId, fullName, email, password, Role.ADMIN);
        this.privilegeLevel = privilegeLevel;
    }

    public int getPrivilegeLevel() {
        return privilegeLevel;
    }

    public void setPrivilegeLevel(int privilegeLevel) {
        this.privilegeLevel = privilegeLevel;
    }
}
