package com.silvercare.service;

import com.silvercare.dao.HabitDAO;
import com.silvercare.dao.HabitRecordDAO;
import com.silvercare.model.Habit;
import com.silvercare.model.HabitRecord;
import com.silvercare.model.enums.HabitStatus;
import com.silvercare.model.enums.HabitType;
import com.silvercare.model.enums.TargetType;
import com.silvercare.model.enums.ActionType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class HabitService {

    private final HabitDAO habitDAO = new HabitDAO();
    private final HabitRecordDAO habitRecordDAO = new HabitRecordDAO();
    private final OperationLogService operationLogService = new OperationLogService();

    public boolean createHabit(int userId,
                               String title,
                               HabitType type,
                               LocalTime reminderTime) {

        if (userId <= 0) {
            System.out.println("使用者 ID 不正確");
            return false;
        }

        if (title == null || title.isBlank()) {
            System.out.println("習慣名稱不可為空");
            return false;
        }

        if (type == null) {
            System.out.println("習慣類型不可為空");
            return false;
        }

        Habit habit = new Habit();
        habit.setUserId(userId);
        habit.setTitle(title);
        habit.setType(type);
        habit.setReminderTime(reminderTime);
        habit.setActive(true);

        boolean success =  habitDAO.create(habit);
        if (success) {
            operationLogService.log(
                    userId,
                    ActionType.CREATE,
                    TargetType.HABIT,
                    0
            );
        }
        return success;
    }
    public boolean deleteHabit(int userId, int habitId) {
        Habit habit =
                habitDAO.findById(habitId);

        // 習慣不存在
        if (habit == null) {

            System.out.println("找不到此習慣");

            return false;
        }

        // 權限檢查
        if (habit.getUserId() != userId) {

            System.out.println("無權限刪除此習慣");

            return false;
        }

        // 軟刪除
        boolean success = habitDAO.softDelete(habitId);

        // 成功後寫 Audit Log
        if (success) {

            operationLogService.log(
                    userId,
                    ActionType.DELETE,
                    TargetType.HABIT,
                    habitId
            );
        }

        return success;
    }

    public List<Habit> getUserHabits(int userId) {
        return habitDAO.findByUserId(userId);
    }

    public boolean completeTodayHabit(int userId, int habitId, String note) {
        if (userId <= 0) {
            System.out.println("使用者 ID 不正確");
            return false;
        }

        if (habitId <= 0) {
            System.out.println("習慣 ID 不正確");
            return false;
        }

        Habit habit = habitDAO.findById(habitId);

        if (habit == null) {
            System.out.println("找不到此習慣");
            return false;
        }

        if (habit.getUserId() != userId) {
            System.out.println("你只能打卡自己的習慣");
            return false;
        }

        if (!habit.isActive()) {
            System.out.println("此習慣已停用");
            return false;
        }

        LocalDate today = LocalDate.now();

        HabitRecord existingRecord =
                habitRecordDAO.findByHabitIdAndDate(habitId, today);

        boolean success;
        if (existingRecord == null) {
            HabitRecord record = new HabitRecord();
            record.setHabitId(habitId);
            record.setRecordDate(today);
            record.setStatus(HabitStatus.COMPLETED);
            record.setNote(note);

            success = habitRecordDAO.create(record);
        } else {
            success = habitRecordDAO.updateStatus(
                    habitId,
                    today,
                    HabitStatus.COMPLETED,
                    note
            );
        }

        if (success) {
            operationLogService.log(
                    userId,
                    ActionType.CHECKIN,
                    TargetType.HABIT,
                    habitId
            );
        }

        return success;
    }

    public List<HabitRecord> getHabitRecords(int habitId) {
        return habitRecordDAO.findByHabitId(habitId);
    }

    public boolean deleteHabit(int habitId) {
        if (habitId <= 0) {
            System.out.println("習慣 ID 不正確");
            return false;
        }

        return habitDAO.softDelete(habitId);
    }
}