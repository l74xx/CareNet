package com.silvercare.model.enums;

/**
 * 活動狀態列舉
 */
public enum ActivityStatus {

    OPEN("開放報名", "🟢"),
    FULL("活動額滿", "🔴"),
    CANCELLED("已取消", "⚫");

    private final String displayName;
    private final String icon;

    ActivityStatus(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public static ActivityStatus fromString(String s) {
        if (s == null || s.isBlank()) {
            return OPEN;
        }

        try {
            return valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return OPEN;
        }
    }
}