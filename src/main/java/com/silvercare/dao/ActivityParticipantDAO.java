package com.silvercare.dao;

import com.silvercare.config.DatabaseConfig;
import com.silvercare.model.ActivityParticipant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityParticipantDAO {

    public boolean joinActivity(int activityId, int userId) {
        String sql = """
                INSERT INTO activity_participants (activity_id, user_id)
                VALUES (?, ?)
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, activityId);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("報名活動失敗：" + e.getMessage());
            return false;
        }
    }

    public boolean exists(int activityId, int userId) {
        String sql = """
                SELECT 1
                FROM activity_participants
                WHERE activity_id = ? AND user_id = ?
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, activityId);
            stmt.setInt(2, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("檢查活動報名狀態失敗：" + e.getMessage());
            return false;
        }
    }

    public List<ActivityParticipant> findByActivityId(int activityId) {
        List<ActivityParticipant> participants = new ArrayList<>();

        String sql = """
                SELECT *
                FROM activity_participants
                WHERE activity_id = ?
                ORDER BY joined_at
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, activityId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    participants.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("查詢活動參與者失敗：" + e.getMessage());
        }

        return participants;
    }

    public int countByActivityId(int activityId) {
        String sql = """
                SELECT COUNT(*) AS total
                FROM activity_participants
                WHERE activity_id = ?
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, activityId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (SQLException e) {
            System.out.println("查詢活動人數失敗：" + e.getMessage());
        }

        return 0;
    }

    private ActivityParticipant mapRow(ResultSet rs) throws SQLException {
        return new ActivityParticipant(
                rs.getInt("id"),
                rs.getInt("activity_id"),
                rs.getInt("user_id"),
                rs.getTimestamp("joined_at").toLocalDateTime()
        );
    }
}