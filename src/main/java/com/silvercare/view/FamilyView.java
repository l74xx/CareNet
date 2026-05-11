package com.silvercare.view;

import com.silvercare.dao.UserDAO;
import com.silvercare.model.FamilyLink;
import com.silvercare.model.Habit;
import com.silvercare.model.HealthRecord;
import com.silvercare.model.User;
import com.silvercare.service.FamilyService;
import com.silvercare.service.HabitService;
import com.silvercare.service.HealthService;
import com.silvercare.model.enums.ActionType;
import com.silvercare.service.OperationLogService;
import com.silvercare.model.enums.TargetType;

import java.util.List;
import java.util.Scanner;
import com.silvercare.util.SessionManager;

public class FamilyView {

    private final Scanner scanner;
    private final User currentUser;

    private final FamilyService familyService = new FamilyService();
    private final HealthService healthService = new HealthService();
    private final HabitService habitService = new HabitService();
    private final UserDAO userDAO = new UserDAO();
    private final OperationLogService operationLogService = new OperationLogService();

    public FamilyView(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("\n===== 家屬功能選單 =====");
            System.out.println("1. 查看綁定長者");
            System.out.println("2. 查看長者健康紀錄");
            System.out.println("3. 查看長者習慣清單");
            System.out.println("0. 登出");
            System.out.print("請選擇：");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showLinkedElders();
                    break;
                case "2":
                    showElderHealthRecords();
                    break;
                case "3":
                    showElderHabits();
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

    private List<FamilyLink> getMyLinks() {
        return familyService.getEldersByFamilyId(currentUser.getId());
    }

    private List<FamilyLink> showLinkedEldersAndReturn() {
        List<FamilyLink> links = getMyLinks();

        if (links.isEmpty()) {
            System.out.println("目前沒有綁定長者。");
            return links;
        }

        for (FamilyLink link : links) {
            User elder = userDAO.findById(link.getElderId());
            String elderName = elder == null ? "未知長者" : elder.getFullName();

            System.out.println(
                    "長者 ID：" + link.getElderId()
                            + " / 姓名：" + elderName
                            + " / 關係：" + link.getRelationship()
            );
        }

        return links;
    }

    private void showLinkedElders() {
        showLinkedEldersAndReturn();
    }

    private boolean isLinkedElder(int elderId) {
        List<FamilyLink> links = getMyLinks();

        for (FamilyLink link : links) {
            if (link.getElderId() == elderId) {
                return true;
            }
        }

        return false;
    }

    private void showElderHealthRecords() {
        List<FamilyLink> links = showLinkedEldersAndReturn();

        if (links.isEmpty()) {
            return;
        }

        System.out.print("請輸入要查看的長者 ID：");
        int elderId = readInt();

        if (!isLinkedElder(elderId)) {
            System.out.println("你只能查看已綁定長者的紀錄。");
            return;
        }

        List<HealthRecord> records = healthService.getUserHealthRecords(elderId);

        if (records.isEmpty()) {
            System.out.println("此長者目前沒有健康紀錄。");
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

    private void showElderHabits() {
        List<FamilyLink> links = showLinkedEldersAndReturn();

        if (links.isEmpty()) {
            return;
        }

        System.out.print("請輸入要查看的長者 ID：");
        int elderId = readInt();

        if (!isLinkedElder(elderId)) {
            System.out.println("你只能查看已綁定長者的習慣。");
            return;
        }

        List<Habit> habits = habitService.getUserHabits(elderId);

        if (habits.isEmpty()) {
            System.out.println("此長者目前沒有習慣。");
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

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }
}