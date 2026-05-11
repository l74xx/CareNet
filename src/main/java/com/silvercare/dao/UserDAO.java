package com.silvercare.dao;

import com.silvercare.config.DatabaseConfig;
import com.silvercare.model.User;
import com.silvercare.model.enums.UserRole;

import java.sql.*;

/**
 * 使用者 DAO —
 *
 * 📝 DAO 的職責：
 *    - 只負責「資料庫存取」（SQL 操作）
 *    - 不做業務邏輯判斷（那是 Service 的工作）
 *    - 每個 public 方法對應一種 SQL 操作
 */


public class UserDAO implements BaseDAO<User>{


    @Override
    public boolean create(User user) {
        String sql = """
                INSERT INTO users (username, password_hash, full_name, role, phone)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole().name());
            ps.setString(5, user.getPhone());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("新增使用者失敗：" + e.getMessage());
            return false;
        }
    }

    /** 依 ID 查詢使用者 */
    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ 查詢使用者失敗: " + e.getMessage());
        }
        return null;
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("查詢使用者失敗：" + e.getMessage());
        }

        return null;
    }


    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getString("full_name"),
                UserRole.fromString(rs.getString("role")),
                rs.getString("phone"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
