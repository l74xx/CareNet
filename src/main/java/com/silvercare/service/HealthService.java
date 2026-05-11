package com.silvercare.service;

import com.silvercare.dao.HealthRecordDAO;
import com.silvercare.model.HealthRecord;
import com.silvercare.model.enums.HealthRecordType;
import com.silvercare.model.enums.TargetType;
import com.silvercare.model.enums.ActionType;

import java.util.List;

public class HealthService {

    private final HealthRecordDAO healthRecordDAO = new HealthRecordDAO();
    private final OperationLogService operationLogService = new OperationLogService();

    public boolean addBloodPressureRecord(int userId,
                                          int systolic,
                                          int diastolic,
                                          String note) {

        if (userId <= 0) {
            System.out.println("使用者 ID 不正確");
            return false;
        }

        if (systolic <= 0 || diastolic <= 0) {
            System.out.println("血壓數值不可小於等於 0");
            return false;
        }

        HealthRecord record = new HealthRecord();
        record.setUserId(userId);
        record.setRecordType(HealthRecordType.BLOOD_PRESSURE);
        record.setSystolic(systolic);
        record.setDiastolic(diastolic);
        record.setBloodSugar(null);
        record.setNote(note);

        boolean success = healthRecordDAO.create(record);

        if (success) {
            operationLogService.log(
                    userId,
                    ActionType.CREATE,
                    TargetType.HEALTH_RECORD,
                    0);

            checkBloodPressureAlert(systolic, diastolic);
        }

        return success;
    }

    public boolean addBloodSugarRecord(int userId,
                                       double bloodSugar,
                                       String note) {

        if (userId <= 0) {
            System.out.println("使用者 ID 不正確");
            return false;
        }

        if (bloodSugar <= 0) {
            System.out.println("血糖數值不可小於等於 0");
            return false;
        }

        HealthRecord record = new HealthRecord();
        record.setUserId(userId);
        record.setRecordType(HealthRecordType.BLOOD_SUGAR);
        record.setSystolic(null);
        record.setDiastolic(null);
        record.setBloodSugar(bloodSugar);
        record.setNote(note);

        boolean success = healthRecordDAO.create(record);

        if (success) {
            operationLogService.log(
                    userId,
                    ActionType.CREATE,
                    TargetType.HEALTH_RECORD,
                    0);

            checkBloodSugarAlert(bloodSugar);
        }

        return success;
    }

    public List<HealthRecord> getUserHealthRecords(int userId) {
        return healthRecordDAO.findByUserId(userId);
    }

    private void checkBloodPressureAlert(int systolic, int diastolic) {
        if (systolic >= 140 || diastolic >= 90) {
            System.out.println("⚠ 血壓偏高，建議通知家屬並持續追蹤。");
        }
    }

    private void checkBloodSugarAlert(double bloodSugar) {
        if (bloodSugar >= 126) {
            System.out.println("⚠ 血糖偏高，請注意飲食並依醫囑追蹤。");
        }
    }
}