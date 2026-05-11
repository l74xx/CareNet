package com.silvercare.dao;

import com.silvercare.config.DatabaseConfig;
import com.silvercare.model.Habit;
import com.silvercare.model.enums.HabitType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitDAO implements BaseDAO<Habit>{

    @Override
    public boolean create(Habit habit) {
        String sql = """
                INSERT INTO habits (user_id, title, habit_type, reminder_time, active)
                VALUES (?, ?, ?, ?, true)
                """;

        if (habit.getType() == null) {
            System.out.println("習慣類型不可為空");
            return false;
        }

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, habit.getUserId());
            ps.setString(2, habit.getTitle());
            ps.setString(3, habit.getType().name());

            if (habit.getReminderTime() == null) {
                ps.setNull(4, Types.TIME);
            } else {
                ps.setTime(4, Time.valueOf(habit.getReminderTime()));
            }

            ps.setBoolean(5, habit.isActive());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("新增習慣失敗：" + e.getMessage());
            return false;
        }
    }

    @Override
    public Habit findById(int id) {
        String sql = """
                     SELECT * FROM habits 
                     WHERE id = ? 
                     AND active = true
                     """;

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
            System.out.println("查詢習慣失敗：" + e.getMessage());
        }

        return null;
    }

    public List<Habit> findByUserId(int userId) {
        List<Habit> habits = new ArrayList<>();

        String sql = """
                SELECT *
                FROM habits
                WHERE user_id = ? 
                AND active = TRUE
                ORDER BY id
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    habits.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("查詢習慣失敗：" + e.getMessage());
        }

        return habits;
    }

    public boolean softDelete(int id) {
        String sql = """
                        UPDATE habits 
                        SET active = FALSE 
                        WHERE id = ?
                        """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("刪除習慣失敗：" + e.getMessage());
        }
        return false;
    }

    private Habit mapRow(ResultSet rs) throws SQLException {
        Time reminder = rs.getTime("reminder_time");

        return new Habit(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("title"),
                HabitType.fromString(rs.getString("habit_type")),
                reminder == null ? null : reminder.toLocalTime(),
                rs.getBoolean("active"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}