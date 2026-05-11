package com.silvercare.view;

import com.silvercare.model.Activity;
import com.silvercare.model.Habit;
import com.silvercare.model.HealthRecord;
import com.silvercare.model.User;
import com.silvercare.model.enums.HabitType;
import com.silvercare.service.ActivityService;
import com.silvercare.service.HabitService;
import com.silvercare.service.HealthService;
import com.silvercare.model.enums.ActionType;
import com.silvercare.service.OperationLogService;
import com.silvercare.model.enums.TargetType;

import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import com.silvercare.util.SessionManager;

public class ElderView {

    private final Scanner scanner;
    private final User currentUser;

    private final HabitService habitService = new HabitService();
    private final HealthService healthService = new HealthService();
    private final ActivityService activityService = new ActivityService();
    private final OperationLogService operationLogService = new OperationLogService();

    public ElderView(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== 長者功能選單 =====");
            System.out.println("1. 查看我的習慣");
            System.out.println("2. 新增習慣");
            System.out.println("3. 刪除習慣");
            System.out.println("4. 今日習慣打卡");
            System.out.println("5. 新增血壓紀錄");
            System.out.println("6. 新增血糖紀錄");
            System.out.println("7. 查看健康紀錄");
            System.out.println("8. 查看社區活動");
            System.out.println("9. 報名社區活動");
            System.out.println("0. 登出");
            System.out.print("請選擇：");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showHabits();
                    break;
                case "2":
                    createHabit();
                    break;
                case "3":
                    deleteHabit();
                case "4":
                    completeHabit();
                    break;
                case "5":
                    addBloodPressure();
                    break;
                case "6":
                    addBloodSugar();
                    break;
                case "7":
                    showHealthRecords();
                    break;
                case "8":
                    showActivities();
                    break;
                case "9":
                    joinActivity();
                    break;
                case "0":
                    operationLogService.log(
                            currentUser.getId(),
                            ActionType.LOGOUT,
                            TargetType.USER,
                            currentUser.getId()
                    );
                    SessionManager.logout();
                    System.out.println("👋 已登出");
                    running = false;
                    break;
                default:
                    System.out.println("請輸入有效選項。");
            }
        }
    }

    private void showHabits() {
        List<Habit> habits = habitService.getUserHabits(currentUser.getId());

        if (habits.isEmpty()) {
            System.out.println("目前沒有習慣。");
            return;
        }

        for (Habit habit : habits) {
            System.out.println(
                    habit.getId() + ". "
                            + habit.getTitle()
                            + " / " + habit.getType().getIcon()
                            + " " + habit.getType().getDisplayName()
                            + " / " + habit.getReminderTime()
            );
        }
    }

    private void createHabit() {
        System.out.print("習慣名稱：");
        String title = scanner.nextLine();

        System.out.println("習慣類型：");
        System.out.println("1. 喝水");
        System.out.println("2. 吃藥");
        System.out.println("3. 散步");
        System.out.println("4. 睡眠");
        System.out.println("5. 復健");
        System.out.print("請選擇：");

        String typeChoice = scanner.nextLine();
        HabitType type;

        switch (typeChoice) {
            case "1":
                type = HabitType.WATER;
                break;
            case "2":
                type = HabitType.MEDICINE;
                break;
            case "3":
                type = HabitType.WALK;
                break;
            case "4":
                type = HabitType.SLEEP;
                break;
            case "5":
                type = HabitType.REHAB;
                break;
            default:
                System.out.println("類型錯誤。");
                return;
        }

        System.out.print("提醒時間，例如 08:00：");
        String timeText = scanner.nextLine();

        LocalTime reminderTime;

        try {
            reminderTime = LocalTime.parse(timeText);
        } catch (Exception e) {
            System.out.println("時間格式錯誤。");
            return;
        }

        boolean success = habitService.createHabit(
                currentUser.getId(),
                title,
                type,
                reminderTime
        );

        System.out.println(success ? "新增習慣成功。" : "新增習慣失敗。");
    }

    private void deleteHabit() {

        System.out.print("請輸入習慣 ID：");

        int habitId =
                Integer.parseInt(
                        scanner.nextLine()
                );

        System.out.print(
                "確定刪除？(y/n)："
        );

        String confirm =
                scanner.nextLine();

        if (!confirm.equalsIgnoreCase("y")) {

            System.out.println("已取消");

            return;
        }

        boolean success =
                habitService.deleteHabit(
                        currentUser.getId(),
                        habitId
                );

        if (success) {

            System.out.println("✅ 習慣已刪除");

        } else {

            System.out.println("❌ 刪除失敗");
        }
    }

    private void completeHabit() {
        List<Habit> habits = habitService.getUserHabits(currentUser.getId());

        if (habits.isEmpty()) {
            System.out.println("目前沒有習慣可以打卡。");
            return;
        }

        for (Habit habit : habits) {
            System.out.println(
                    habit.getId() + ". "
                            + habit.getTitle()
                            + " / " + habit.getType().getIcon()
                            + " " + habit.getType().getDisplayName()
                            + " / " + habit.getReminderTime()
            );
        }

        System.out.print("請輸入要打卡的習慣 ID：");
        int habitId = readInt();

        System.out.print("備註：");
        String note = scanner.nextLine();

        boolean success = habitService.completeTodayHabit(
                currentUser.getId(),
                habitId,
                note
        );

        System.out.println(success ? "今日打卡成功。" : "今日打卡失敗。");
    }

    private void addBloodPressure() {
        System.out.print("收縮壓：");
        int systolic = readInt();

        System.out.print("舒張壓：");
        int diastolic = readInt();

        System.out.print("備註：");
        String note = scanner.nextLine();

        boolean success = healthService.addBloodPressureRecord(
                currentUser.getId(),
                systolic,
                diastolic,
                note
        );

        System.out.println(success ? "血壓紀錄新增成功。" : "血壓紀錄新增失敗。");
    }

    private void addBloodSugar() {
        System.out.print("血糖：");
        double bloodSugar = readDouble();

        System.out.print("備註：");
        String note = scanner.nextLine();

        boolean success = healthService.addBloodSugarRecord(
                currentUser.getId(),
                bloodSugar,
                note
        );

        System.out.println(success ? "血糖紀錄新增成功。" : "血糖紀錄新增失敗。");
    }

    private void showHealthRecords() {
        List<HealthRecord> records =
                healthService.getUserHealthRecords(currentUser.getId());

        if (records.isEmpty()) {
            System.out.println("目前沒有健康紀錄。");
            return;
        }

        for (HealthRecord record : records) {
            if (record.getBloodSugar() != null) {
                System.out.println(
                        record.getId() + ". "
                                + record.getRecordType().getIcon()
                                + " " + record.getRecordType().getDisplayName()
                                + "：" + record.getBloodSugar()
                                + " / " + record.getRecordedAt()
                                + " / 備註：" + record.getNote()
                );
            } else {
                System.out.println(
                        record.getId() + ". "
                                + record.getRecordType().getIcon()
                                + " " + record.getRecordType().getDisplayName()
                                + "：" + record.getSystolic() + "/"
                                + record.getDiastolic()
                                + " / " + record.getRecordedAt()
                                + " / 備註：" + record.getNote()
                );
            }
        }
    }

    private void showActivities() {
        List<Activity> activities = activityService.getOpenActivities();

        if (activities.isEmpty()) {
            System.out.println("目前沒有開放活動。");
            return;
        }

        for (Activity activity : activities) {
            System.out.println(
                    activity.getId() + ". "
                            + activity.getTitle()
                            + " / " + activity.getLocation()
                            + " / " + activity.getActivityTime()
                            + " / " + activity.getStatus().getIcon()
                            + " " + activity.getStatus().getDisplayName()
            );
        }
    }

    private void joinActivity() {
        List<Activity> activities = activityService.getOpenActivities();

        if (activities.isEmpty()) {
            System.out.println("目前沒有開放活動。");
            return;
        }

        for (Activity activity : activities) {
            System.out.println(
                    activity.getId() + ". "
                            + activity.getTitle()
                            + " / " + activity.getLocation()
                            + " / " + activity.getActivityTime()
                            + " / " + activity.getStatus().getIcon()
                            + " " + activity.getStatus().getDisplayName()
            );
        }

        System.out.print("請輸入活動 ID：");
        int activityId = readInt();

        boolean success = activityService.joinActivity(
                activityId,
                currentUser.getId()
        );

        System.out.println(success ? "活動報名成功。" : "活動報名失敗。");
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }

    private double readDouble() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }
}