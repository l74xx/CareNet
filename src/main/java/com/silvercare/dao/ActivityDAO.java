package com.silvercare.dao;

import com.silvercare.config.DatabaseConfig;
import com.silvercare.model.Activity;
import com.silvercare.model.enums.ActivityStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAO implements BaseDAO<Activity> {

    @Override
    public boolean create(Activity activity) {
        String sql = """
                INSERT INTO activities
                (title, description, location, activity_time, max_participants, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, activity.getTitle());
            ps.setString(2, activity.getDescription());
            ps.setString(3, activity.getLocation());
            ps.setTimestamp(4, Timestamp.valueOf(activity.getActivityTime()));

            int maxParticipants = activity.getMaxParticipants() <= 0
                    ? 20
                    : activity.getMaxParticipants();

            String status = activity.getStatus() == null
                    ? ActivityStatus.OPEN.name()
                    : activity.getStatus().name();

            ps.setInt(5, maxParticipants);
            ps.setString(6, status);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("新增活動失敗：" + e.getMessage());
            return false;
        }
    }

    public List<Activity> findAllOpen() {
        List<Activity> activities = new ArrayList<>();

        String sql = """
                SELECT *
                FROM activities
                WHERE status = 'OPEN'
                ORDER BY activity_time
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                activities.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.out.println("查詢活動失敗：" + e.getMessage());
        }

        return activities;
    }

    @Override
    public Activity findById(int id) {
        String sql = "SELECT * FROM activities WHERE id = ?";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("查詢活動失敗：" + e.getMessage());
        }

        return null;
    }

    private Activity mapRow(ResultSet rs) throws SQLException {
        return new Activity(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getTimestamp("activity_time").toLocalDateTime(),
                rs.getInt("max_participants"),
                ActivityStatus.fromString(rs.getString("status")),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}