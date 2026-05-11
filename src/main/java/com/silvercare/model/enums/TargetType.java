package com.silvercare.model.enums;

public enum TargetType {
    USER("使用者"),
    HABIT("習慣"),
    HEALTH_RECORD("健康記錄"),
    ACTIVITY("活動"),
    FAMILY_LINK("親屬關係"),
    INVALID("無效");

    private String displayName;

    TargetType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TargetType fromString(String value) {
        if (value == null || value.isBlank()) {
            return INVALID;
        }
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e){
            return INVALID;
        }
    }
}



