package com.silvercare.model.enums;

/**
 * 習慣完成狀態列舉
 */
public enum HabitStatus {

    PENDING("尚未完成", "🟡"),
    COMPLETED("已完成", "✅"),
    MISSED("未完成", "❌");

    private final String displayName;
    private final String icon;

    HabitStatus(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public static HabitStatus fromString(String s) {
        if (s == null || s.isBlank()) {
            return PENDING;
        }

        try {
            return valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }
}