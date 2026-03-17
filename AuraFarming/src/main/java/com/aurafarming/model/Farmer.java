package com.aurafarming.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class Farmer extends User {
    @Serial
    private static final long serialVersionUID = 1L;

    private District district;
    private String phone;
    private List<String> farmIds = new ArrayList<>();

    public Farmer() {
    }

    public Farmer(String userId, String fullName, String email, String password, District district, String phone) {
        super(userId, fullName, email, password, Role.FARMER);
        this.district = district;
        this.phone = phone;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getFarmIds() {
        return farmIds;
    }

    public void setFarmIds(List<String> farmIds) {
        this.farmIds = farmIds;
    }
}
