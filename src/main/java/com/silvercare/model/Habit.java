package com.silvercare.model;

import com.silvercare.model.enums.HabitType;
import java.time.LocalDateTime;
import java.time.LocalTime;

/** id : 習慣 id
 * user_id : 所屬使用者
 * title : 習慣名稱
 * type : 習慣類型
 * reminderTime : 提醒時間
 * active : Soft Delete 狀態
 */

public class Habit {
    private int id;
    private int userId;
    private String title;
    private HabitType type;
    private LocalTime reminderTime;
    private boolean active;
    private LocalDateTime createdAt;

    public Habit() {}

    public Habit(int userId, String title, HabitType type, LocalTime reminderTime) {
        this.userId = userId;
        this.title = title;
        this.type = type;
        this.reminderTime = reminderTime;
        this.active = true;
    }

    public Habit(int id, int userId, String title,
                 HabitType type, LocalTime reminderTime,
                 boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.type = type;
        this.reminderTime = reminderTime;
        this.active = active;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public HabitType getType() {
        return type;
    }

    public void setType(HabitType type) {
        this.type = type;
    }


    public LocalTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalTime reminderTime) {
        this.reminderTime = reminderTime;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}