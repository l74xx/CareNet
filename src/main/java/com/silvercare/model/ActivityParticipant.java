package com.silvercare.model;

import java.time.LocalDateTime;

public class ActivityParticipant {
    private int id;
    private int activityId;
    private int userId;
    private LocalDateTime joinedAt;

    public ActivityParticipant(int activityId, int userId) {
        this.activityId = activityId;
        this.userId = userId;
    }

    public ActivityParticipant(int id, int activityId,
                               int userId, LocalDateTime joinedAt) {
        this.id = id;
        this.activityId = activityId;
        this.userId = userId;
        this.joinedAt = joinedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}