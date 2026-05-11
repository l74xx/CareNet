package com.silvercare.model.enums;

/**
 * 習慣類別列舉
 *
 * 📝 同學請替換成你的專題需要的分類，例如：
 *    - 玫瑰：HYBRID_TEA, FLORIBUNDA, CLIMBING...
 *    - 房間：SINGLE, DOUBLE, SUITE, DELUXE...
 *    - 帳單：TELECOM, ELECTRICITY, MANAGEMENT_FEE...
 *    - RPG：WEAPON, ARMOR, POTION, MATERIAL...
 */

public enum HabitType {

    WATER("喝水", "💧"),
    MEDICINE("吃藥", "💊"),
    WALK("散步", "🚶"),
    SLEEP("睡眠", "😴"),
    REHAB("復健", "🧘");

    private final String displayName;
    private final String icon;

    HabitType(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public static HabitType fromString(String s) {
        if (s == null || s.isBlank()) {
            return WATER;
        }

        try {
            return valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return WATER;
        }
    }
}
