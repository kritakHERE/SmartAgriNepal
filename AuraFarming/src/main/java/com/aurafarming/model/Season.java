package com.aurafarming.model;

public enum Season {
    SPRING("Basant (Spring)"),
    SUMMER("Grishma (Summer)"),
    MONSOON("Barkha (Monsoon)"),
    AUTUMN("Sharad (Autumn)"),
    WINTER("Shishir (Winter)");

    private final String displayName;

    Season(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
