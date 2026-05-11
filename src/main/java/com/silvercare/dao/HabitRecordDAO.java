package com.silvercare.dao;

import com.silvercare.config.DatabaseConfig;
import com.silvercare.model.HabitRecord;
import com.silvercare.model.enums.HabitStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HabitRecordDAO {

    public boolean create(HabitRecord record) {
        String sql = """
                INSERT INTO habit_records (habit_id, record_date, status, note)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            LocalDate targetDate = record.getRecordDate() == null
                    ? LocalDate.now()
                    : record.getRecordDate();

            String status = record.getStatus() == null
                    ? HabitStatus.PENDING.name()
                    : record.getStatus().name();

            ps.setInt(1, record.getHabitId());
            ps.setDate(2, Date.valueOf(targetDate));
            ps.setString(3, status);
            ps.setString(4, record.getNote());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("新增習慣紀錄失敗：" + e.getMessage());
            return false;
        }
    }

    public boolean updateStatus(int habitId, LocalDate recordDate,
                                HabitStatus status, String note) {
        String sql = """
                UPDATE habit_records
                SET status = ?, note = ?
                WHERE habit_id = ? AND record_date = ?
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            LocalDate targetDate = recordDate == null
                    ? LocalDate.now()
                    : recordDate;

            String targetStatus = status == null
                    ? HabitStatus.PENDING.name()
                    : status.name();

            ps.setString(1, targetStatus);
            ps.setString(2, note);
            ps.setInt(3, habitId);
            ps.setDate(4, Date.valueOf(targetDate));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("更新習慣紀錄失敗：" + e.getMessage());
            return false;
        }
    }

    public HabitRecord findByHabitIdAndDate(int habitId, LocalDate recordDate) {
        String sql = """
                SELECT *
                FROM habit_records
                WHERE habit_id = ? AND record_date = ?
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            LocalDate targetDate = recordDate == null
                    ? LocalDate.now()
                    : recordDate;

            ps.setInt(1, habitId);
            ps.setDate(2, Date.valueOf(targetDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("查詢習慣紀錄失敗：" + e.getMessage());
        }

        return null;
    }

    public List<HabitRecord> findByHabitId(int habitId) {
        List<HabitRecord> records = new ArrayList<>();

        String sql = """
                SELECT *
                FROM habit_records
                WHERE habit_id = ?
                ORDER BY record_date DESC
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, habitId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("查詢習慣紀錄失敗：" + e.getMessage());
        }

        return records;
    }

    private HabitRecord mapRow(ResultSet rs) throws SQLException {
        return new HabitRecord(
                rs.getInt("id"),
                rs.getInt("habit_id"),
                rs.getDate("record_date").toLocalDate(),
                HabitStatus.fromString(rs.getString("status")),
                rs.getString("note"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}