package com.silvercare.model;

import com.silvercare.model.enums.ActivityStatus;
import java.time.LocalDateTime;

/**
 * id : 活動 id
 * title : 活動名稱
 * description : 活動說明
 * activity Time : 活動時間
 */

public class Activity {
    private int id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime activityTime;
    private int maxParticipants;
    private ActivityStatus status;
    private LocalDateTime createdAt;

    public Activity() {}

    public Activity(String title, String description, String location,
                    LocalDateTime activityTime, int maxParticipants) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.activityTime = activityTime;
        this.maxParticipants = maxParticipants;
    }

    public Activity(int id, String title,
                    String description, String location,
                    LocalDateTime activityTime, int maxParticipants,
                    ActivityStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.activityTime = activityTime;
        this.maxParticipants = maxParticipants;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public LocalDateTime getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(LocalDateTime activityTime) {
        this.activityTime = activityTime;
    }


    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }


    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}