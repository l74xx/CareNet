package com.silvercare.dao;

import com.silvercare.config.DatabaseConfig;
import com.silvercare.model.HealthRecord;
import com.silvercare.model.enums.HealthRecordType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HealthRecordDAO {

    public boolean create(HealthRecord record) {
        String sql = """
                INSERT INTO health_records
                (user_id, record_type, systolic, diastolic, blood_sugar, note)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        if (record.getRecordType() == null) {
            System.out.println("健康紀錄類型不可為空");
            return false;
        }

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, record.getUserId());
            ps.setString(2, record.getRecordType().name());

            if (record.getSystolic() == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, record.getSystolic());
            }

            if (record.getDiastolic() == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, record.getDiastolic());
            }

            if (record.getBloodSugar() == null) {
                ps.setNull(5, Types.DECIMAL);
            } else {
                ps.setDouble(5, record.getBloodSugar());
            }

            ps.setString(6, record.getNote());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("新增健康紀錄失敗：" + e.getMessage());
            return false;
        }
    }

    public List<HealthRecord> findByUserId(int userId) {
        List<HealthRecord> records = new ArrayList<>();

        String sql = """
                SELECT *
                FROM health_records
                WHERE user_id = ?
                ORDER BY recorded_at DESC
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("查詢健康紀錄失敗：" + e.getMessage());
        }

        return records;
    }

    private HealthRecord mapRow(ResultSet rs) throws SQLException {
        Integer systolic = rs.getObject("systolic") == null
                ? null
                : rs.getInt("systolic");

        Integer diastolic = rs.getObject("diastolic") == null
                ? null
                : rs.getInt("diastolic");

        Double bloodSugar = rs.getObject("blood_sugar") == null
                ? null
                : rs.getDouble("blood_sugar");

        return new HealthRecord(
                rs.getInt("id"),
                rs.getInt("user_id"),
                HealthRecordType.fromString(rs.getString("record_type")),
                systolic,
                diastolic,
                bloodSugar,
                rs.getString("note"),
                rs.getTimestamp("recorded_at").toLocalDateTime()
        );
    }
}