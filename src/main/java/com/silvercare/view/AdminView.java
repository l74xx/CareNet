package com.silvercare.view;

import com.silvercare.dao.ActivityDAO;
import com.silvercare.dao.ActivityParticipantDAO;
import com.silvercare.dao.UserDAO;
import com.silvercare.model.Activity;
import com.silvercare.model.ActivityParticipant;
import com.silvercare.model.User;
import com.silvercare.service.ActivityService;
import com.silvercare.model.enums.ActionType;
import com.silvercare.service.OperationLogService;
import com.silvercare.model.enums.TargetType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import com.silvercare.util.SessionManager;

public class AdminView {

    private final Scanner scanner;
    private final User currentUser;

    private final ActivityService activityService = new ActivityService();
    private final ActivityDAO activityDAO = new ActivityDAO();
    private final ActivityParticipantDAO participantDAO = new ActivityParticipantDAO();
    private final UserDAO userDAO = new UserDAO();
    private final OperationLogService operationLogService = new OperationLogService();

    public AdminView(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== 管理員功能選單 =====");
            System.out.println("1. 建立社區活動");
            System.out.println("2. 查看開放活動");
            System.out.println("3. 查看活動報名名單");
            System.out.println("0. 登出");
            System.out.print("請選擇：");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createActivity();
                    break;
                case "2":
                    showOpenActivities();
                    break;
                case "3":
                    showActivityParticipants();
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

    private void createActivity() {
        System.out.println("\n===== 建立社區活動 =====");

        System.out.print("活動名稱：");
        String title = scanner.nextLine();

        System.out.print("活動描述：");
        String description = scanner.nextLine();

        System.out.print("地點：");
        String location = scanner.nextLine();

        System.out.print("活動時間，例如 2026-06-01T08:00：");
        String timeText = scanner.nextLine();

        LocalDateTime activityTime;

        try {
            activityTime = LocalDateTime.parse(timeText);
        } catch (Exception e) {
            System.out.println("時間格式錯誤。");
            return;
        }

        System.out.print("人數上限：");
        int maxParticipants = readInt();

        boolean success = activityService.createActivity(
                title,
                description,
                location,
                activityTime,
                maxParticipants
        );

        System.out.println(success ? "活動建立成功。" : "活動建立失敗。");
    }

    private List<Activity> showOpenActivitiesAndReturn() {
        List<Activity> activities = activityService.getOpenActivities();

        if (activities.isEmpty()) {
            System.out.println("目前沒有開放活動。");
            return activities;
        }

        for (Activity activity : activities) {
            int count = participantDAO.countByActivityId(activity.getId());

            System.out.println(
                    activity.getId() + ". "
                            + activity.getTitle()
                            + " / 地點：" + activity.getLocation()
                            + " / 時間：" + activity.getActivityTime()
                            + " / 人數：" + count + "/" + activity.getMaxParticipants()
                            + " / " + activity.getStatus().getIcon()
                            + " " + activity.getStatus().getDisplayName()
            );
        }

        return activities;
    }

    private void showOpenActivities() {
        showOpenActivitiesAndReturn();
    }

    private void showActivityParticipants() {
        List<Activity> activities = showOpenActivitiesAndReturn();

        if (activities.isEmpty()) {
            return;
        }

        System.out.print("請輸入活動 ID：");
        int activityId = readInt();

        Activity activity = activityDAO.findById(activityId);

        if (activity == null) {
            System.out.println("找不到此活動。");
            return;
        }

        List<ActivityParticipant> participants =
                participantDAO.findByActivityId(activityId);

        if (participants.isEmpty()) {
            System.out.println("目前沒有人報名此活動。");
            return;
        }

        System.out.println("活動：" + activity.getTitle());
        System.out.println("=== 報名名單 ===");

        for (ActivityParticipant participant : participants) {
            User user = userDAO.findById(participant.getUserId());
            String name = user == null ? "未知使用者" : user.getFullName();

            System.out.println(
                    "使用者 ID：" + participant.getUserId()
                            + " / 姓名：" + name
                            + " / 報名時間：" + participant.getJoinedAt()
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