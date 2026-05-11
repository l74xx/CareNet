package com.silvercare.service;

import com.silvercare.dao.ActivityDAO;
import com.silvercare.dao.ActivityParticipantDAO;
import com.silvercare.model.Activity;
import com.silvercare.model.enums.ActivityStatus;
import com.silvercare.model.enums.TargetType;
import com.silvercare.model.enums.ActionType;
import com.silvercare.model.User;
import com.silvercare.util.SessionManager;

import java.time.LocalDateTime;
import java.util.List;

public class ActivityService {

    private final ActivityDAO activityDAO = new ActivityDAO();
    private final ActivityParticipantDAO participantDAO = new ActivityParticipantDAO();
    private final OperationLogService operationLogService = new OperationLogService();

    public boolean createActivity(String title,
                                  String description,
                                  String location,
                                  LocalDateTime activityTime,
                                  int maxParticipants) {

        if (title == null || title.isBlank()) {
            System.out.println("活動名稱不可為空");
            return false;
        }

        if (activityTime == null) {
            System.out.println("活動時間不可為空");
            return false;
        }

        Activity activity = new Activity();
        activity.setTitle(title);
        activity.setDescription(description);
        activity.setLocation(location);
        activity.setActivityTime(activityTime);
        activity.setMaxParticipants(maxParticipants <= 0 ? 20 : maxParticipants);
        activity.setStatus(ActivityStatus.OPEN);

        boolean success = activityDAO.create(activity);
        if (success && SessionManager.isLoggedIn()) {
            User currentUser = SessionManager.getCurrentUser();
            operationLogService.log(
                    currentUser.getId(),
                    ActionType.CREATE,
                    TargetType.ACTIVITY,
                    0
            );
        }
        return success;
    }

    public List<Activity> getOpenActivities() {
        return activityDAO.findAllOpen();
    }

    public boolean joinActivity(int activityId, int userId) {
        if (activityId <= 0 || userId <= 0) {
            System.out.println("活動 ID 或使用者 ID 不正確");
            return false;
        }

        Activity activity = activityDAO.findById(activityId);

        if (activity == null) {
            System.out.println("找不到活動");
            return false;
        }

        if (activity.getStatus() != ActivityStatus.OPEN) {
            System.out.println("此活動目前不可報名");
            return false;
        }

        if (participantDAO.exists(activityId, userId)) {
            System.out.println("你已經報名過此活動");
            return false;
        }

        int currentCount = participantDAO.countByActivityId(activityId);

        if (currentCount >= activity.getMaxParticipants()) {
            System.out.println("活動已額滿");
            return false;
        }

        boolean success = participantDAO.joinActivity(activityId, userId);

        if (success) {
            operationLogService.log(
                    userId,
                    ActionType.JOIN,
                    TargetType.ACTIVITY,
                    activityId
            );
        }
        return success;
    }
}