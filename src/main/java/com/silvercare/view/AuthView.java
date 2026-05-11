package com.silvercare.view;

import com.silvercare.model.User;
import com.silvercare.model.enums.UserRole;
import com.silvercare.model.enums.ActionType;
import com.silvercare.service.OperationLogService;
import com.silvercare.service.AuthService;
import com.silvercare.model.enums.TargetType;

import java.util.Scanner;


import com.silvercare.util.SessionManager;

/**
 *  輸入帳密
 *  ↓
 *  UserService.login()
 *  ↓
 *  驗證密碼
 *  ↓
 *  SessionManager.login()
 *  ↓
 *  OperationLog LOGIN
 */

public class AuthView {

    private final Scanner scanner;
    private final AuthService authService = new AuthService();
    private final OperationLogService operationLogService = new OperationLogService();

    public AuthView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void login() {
        System.out.println();
        System.out.println("\n===== 使用者登入 =====");

        System.out.print("帳號：");
        String username = readLine();

        System.out.print("密碼：");
        String password = readPassword().trim();


        User user = authService.login(username, password);

        if (user == null) {
            System.out.println("登入失敗。");
            return;
        }

        SessionManager.login(user);

        operationLogService.log(
                user.getId(),
                ActionType.LOGIN,
                TargetType.USER,
                user.getId()
        );

        System.out.println("登入成功，歡迎 " + user.getFullName());

        switch(user.getRole()) {
            case ELDER -> {
                ElderView elderView = new ElderView(scanner, user);
                elderView.start();
            }

            case FAMILY -> {
                FamilyView familyView = new FamilyView(scanner, user);
                familyView.start();
            }

            case ADMIN -> {
                AdminView adminView = new AdminView(scanner, user);
                adminView.start();
            }

            default -> {
                System.out.println("❌ 未知角色");
            }
        }
    }

    public void register() {
        System.out.println();
        System.out.println("\n===== 使用者註冊 =====");

        System.out.print("帳號：");
        String username = readLine();

        // 密碼即時驗證
        String password;

        while (true) {

            System.out.print("密碼：");
            password = readPassword().trim();

            if (!password.isBlank()
                    && password.length() >= 4
                    && password.length() <= 20) {

                break;
            }

            System.out.println(
                    "❌ 密碼長度需介於 4~20 字元，且不可為空"
            );
        }

        System.out.print("姓名：");
        String fullName = readLine();

        String phone;
        while (true) {

            System.out.print("電話：");
            phone = readLine();

            if (phone.matches("^09\\d{8}$")) {

                break;
            }

            System.out.println(
                    "❌ 電話格式錯誤，請輸入 09 開頭的 10 碼手機號碼"
            );
        }

        System.out.println("角色：");
        System.out.println("1. 長者");
        System.out.println("2. 家屬");
        System.out.println("3. 管理員");
        System.out.print("請選擇：");

        String roleChoice = readLine();
        UserRole role;

        switch (roleChoice) {
            case "1":
                role = UserRole.ELDER;
                break;
            case "2":
                role = UserRole.FAMILY;
                break;
            case "3":
                role = UserRole.ADMIN;
                break;
            default:
                System.out.println("❌ 角色選擇錯誤。");
                return;
        }

        boolean success = authService.register(
                username,
                password,
                fullName,
                role,
                phone
        );

        System.out.println(success ? "✅ 註冊成功，請回到主選單登入。" : "❌ 註冊失敗。");
    }

    private String readLine() {
        return scanner.nextLine().trim();
    }

    private String readPassword() {
        if (System.console() != null) {
            return new String(System.console().readPassword());
        }
        return scanner.nextLine();
    }
}