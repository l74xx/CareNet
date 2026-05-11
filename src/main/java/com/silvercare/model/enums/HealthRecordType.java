package com.silvercare.model.enums;

/**
 * 健康紀錄類型列舉
 */
public enum HealthRecordType {

    BLOOD_PRESSURE("血壓", "🩺"),
    BLOOD_SUGAR("血糖", "🩸");

    private final String displayName;
    private final String icon;

    HealthRecordType(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public static HealthRecordType fromString(String s) {

        if (s == null || s.isBlank()) {
            return BLOOD_PRESSURE;
        }

        try {
            return valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BLOOD_PRESSURE;
        }
    }
}


