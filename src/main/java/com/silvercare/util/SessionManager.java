package com.silvercare.util;

import com.silvercare.model.User;


/**
 * Session 管理器
 *
 * 職責：
 * 1. 保存目前登入中的使用者
 * 2. 提供登入 / 登出功能
 * 3. 提供取得目前使用者的方法
 * 4. 提供登入狀態檢查
 */

public class SessionManager {

    // 目前的使用者
    private static User currentUser;

    // 避免外部 new SessionManager
    private SessionManager() {
    }

    // 登入
    public static void login(User user) {

        currentUser = user;
    }

    // 登出
    public static void logout() {

        currentUser = null;
    }

    // 取得目前的使用者
    public static User getCurrentUser() {

        return currentUser;
    }

    // 是否已登入
    public static boolean isLoggedIn() {

        return currentUser != null;
    }
}