package com.silvercare.model;

import com.silvercare.model.enums.HabitStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;



public class HabitRecord {
    private int id;
    private int habitId;
    private LocalDate recordDate;
    private HabitStatus status;
    private String note;
    private LocalDateTime createdAt;

    public HabitRecord() {}

    public HabitRecord(int habitId, LocalDate recordDate, HabitStatus status, String note, LocalDateTime createdAt) {
        this.habitId = habitId;
        this.recordDate = recordDate;
        this.status = status;
        this.note = note;
    }

    public HabitRecord(int id, int habitId,
                       LocalDate recordDate, HabitStatus status,
                       String note, LocalDateTime createdAt) {
        this.id = id;
        this.habitId = habitId;
        this.recordDate = recordDate;
        this.status = status;
        this.note = note;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getHabitId() {
        return habitId;
    }

    public void setHabitId(int habitId) {
        this.habitId = habitId;
    }


    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }


    public HabitStatus getStatus() {
        return status;
    }

    public void setStatus(HabitStatus status) {
        this.status = status;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}