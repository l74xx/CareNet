package com.silvercare.model;

import com.silvercare.model.enums.HealthRecordType;
import java.time.LocalDateTime;

/**
 * id : 記錄 id
 * userId : 使用者 id
 * type : 血壓/血糖
 * value : 數值
 */

public class HealthRecord {
    private int id;
    private int userId;
    private HealthRecordType recordType;
    private Integer systolic;
    private Integer diastolic;
    private Double bloodSugar;
    private String note;
    private LocalDateTime recordedAt;

    public HealthRecord() {}

    public HealthRecord(int userId, int systolic, int diastolic, String note) {
        this.userId = userId;
        this.recordType = HealthRecordType.BLOOD_PRESSURE;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.note = note;
    }

    public HealthRecord(int userId, double bloodSugar, String note) {
        this.userId = userId;
        this.recordType = HealthRecordType.BLOOD_SUGAR;
        this.bloodSugar = bloodSugar;
        this.note = note;
    }

    public HealthRecord(int id, int userId,
                        HealthRecordType recordType,
                        Integer systolic,
                        Integer diastolic,
                        Double bloodSugar,
                        String note,
                        LocalDateTime recordedAt) {
        this.id = id;
        this.userId = userId;
        this.recordType = recordType;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.bloodSugar = bloodSugar;
        this.note = note;
        this.recordedAt = recordedAt;
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


    public HealthRecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(HealthRecordType recordType) {
        this.recordType = recordType;
    }


    public Integer getSystolic() {
        return systolic;
    }

    public void setSystolic(Integer systolic) {
        this.systolic = systolic;
    }


    public Integer getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(Integer diastolic) {
        this.diastolic = diastolic;
    }


    public Double getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Double bloodSugar) {
        this.bloodSugar = bloodSugar;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}