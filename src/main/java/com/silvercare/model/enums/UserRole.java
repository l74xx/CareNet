package com.silvercare.model.enums;


/**
 * 使用者角色列舉
 *
 * 📝 同學請替換成你的專題狀態，例如：
 *    - 預約：PENDING → CONFIRMED → CANCELLED
 *    - 房間：VACANT → OCCUPIED → CLEANING → INSPECTED
 *    - 任務：TODO → IN_PROGRESS → DONE
 */


public enum UserRole {

    ELDER("長者", "👴"),
    FAMILY("家屬", "👨‍👩‍👧"),
    ADMIN("管理員", "🛠");

    private final String displayName;
    private final String icon;

    UserRole(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public static UserRole fromString(String s) {
        if (s == null || s.isBlank()) {
            return ELDER;
        }

        try {
            return valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ELDER;
        }
    }
}