package com.silvercare.model;

import com.silvercare.model.enums.ActionType;
import com.silvercare.model.enums.TargetType;

import java.time.LocalDateTime;

/**
 * userId : 操作者
 * actionType : 做什麼
 * targetType : 操作哪種資料
 * targetId : 操作哪筆資料
 * createdAt : 時間
 */


/**
 * 操作紀錄
 */
public class OperationLog {

    private int id;

    /**
     * 操作人
     */
    private int userId;

    /**
     * 操作類型
     */
    private ActionType actionType;

    /**
     * 操作目標類型
     */
    private TargetType targetType;

    /**
     * 操作目標 ID
     */
    private int targetId;

    /**
     * 建立時間
     */
    private LocalDateTime createdAt;

    public OperationLog() {
    }

    public OperationLog(
            int userId,
            ActionType actionType,
            TargetType targetType,
            int targetId
    ) {

        this.userId = userId;
        this.actionType = actionType;
        this.targetType = targetType;
        this.targetId = targetId;
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

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(
            ActionType actionType
    ) {

        this.actionType = actionType;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(
            TargetType targetType
    ) {

        this.targetType = targetType;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt
    ) {

        this.createdAt = createdAt;
    }
}