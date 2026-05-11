package com.silvercare.service;

import com.silvercare.dao.UserDAO;
import com.silvercare.model.User;
import com.silvercare.model.enums.UserRole;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    /**
     * 登入
     */
    public User login(String username, String password) {

        if (username == null || username.isBlank()) {
            System.out.println("帳號不可為空");
            return null;
        }

        if (password == null || password.isBlank()) {
            System.out.println("密碼不可為空");
            return null;
        }

        User user = userDAO.findByUsername(username);

        if (user == null) {
            System.out.println("找不到此帳號");
            return null;
        }

        // 簡易 Hash
        String hashedInput =
                Integer.toHexString(password.trim().hashCode());

        if (!user.getPasswordHash().trim().equals(hashedInput)) {
            System.out.println("密碼錯誤");
            return null;
        }

        return user;
    }

    /**
     * 註冊
     */
    public boolean register(String username,
                            String password,
                            String fullName,
                            UserRole role,
                            String phone) {

        //帳號檢查
        if (username == null || username.isBlank()) {
            System.out.println("帳號不可為空");
            return false;
        }

        //密碼檢查
        if (password == null || password.isBlank()) {
            System.out.println("密碼不可為空");
            return false;
        }

        //密碼長度限制
        if (password.length() < 4 || password.length() > 20) {
            System.out.println("密碼長度需介於 4 ~ 20 字元");
            return false;
        }

        // 電話格式檢查
        if (phone != null && !phone.matches("^09\\d{8}$")) {
            System.out.println("電話格式錯誤，請輸入 09 開頭的 10 碼手機號碼");
            return false;
        }

        if (fullName == null || fullName.isBlank()) {
            System.out.println("姓名不可為空");
            return false;
        }

        if (role == null) {
            System.out.println("角色不可為空");
            return false;
        }

        User existingUser = userDAO.findByUsername(username);

        if (existingUser != null) {
            System.out.println("帳號已存在");
            return false;
        }


        String hashedPassword =
                Integer.toHexString(password.hashCode());

        User user = new User(
                username,
                hashedPassword,
                fullName,
                phone,
                role
        );

        return userDAO.create(user);
    }
}