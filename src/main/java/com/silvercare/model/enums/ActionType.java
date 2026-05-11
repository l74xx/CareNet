package com.silvercare.model.enums;

public enum ActionType {
    LOGIN("登入"),
    LOGOUT("登出"),
    CREATE("新增"),
    UPDATE("修改"),
    DELETE("刪除"),
    CHECKIN("打卡"),
    JOIN("報名");

    private final String displayName;

    ActionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ActionType fromString(String value) {
        if (value == null || value.isBlank()) {
            return CREATE;
        }
        try {
            return valueOf(value.toUpperCase());
        } catch(IllegalArgumentException e) {
            return CREATE;
        }
    }
}



