package com.silvercare.model;
import java.time.LocalDateTime;
import com.silvercare.model.enums.UserRole;

/**
 * 使用者模型
 * id : 使用者 id
 * username : 帳號
 * password : 密碼 Hash
 * role : 身份
 */

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String fullName;
    private UserRole role;
    private String phone;
    private LocalDateTime createdAt;

    // === 建構子 ===

    /** 註冊用（還沒有 id） */
    public User(String username, String passwordHash, String fullName, String phone, UserRole role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.fullName = fullName;
        this.role = role;
    }

    /** 從資料庫讀取用 */
    public User(int id, String username, String passwordHash,
                String fullName, UserRole role, String phone,
                LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    // === 業務方法 ===

//    public boolean isAdmin() {
//        return "ADMIN".equalsIgnoreCase(role);
//    }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s)", id, username, role);
    }

    // === Getters & Setters ===
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}









