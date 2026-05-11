package com.silvercare.dao;

import com.silvercare.config.DatabaseConfig;
import com.silvercare.model.FamilyLink;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FamilyLinkDAO {

    public boolean create(FamilyLink link) {
        String sql = """
                INSERT INTO family_links (elder_id, family_id, relationship)
                VALUES (?, ?, ?)
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, link.getElderId());
            stmt.setInt(2, link.getFamilyId());
            stmt.setString(3, link.getRelationship());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("建立家屬綁定失敗：" + e.getMessage());
            return false;
        }
    }

    public List<FamilyLink> findByElderId(int elderId) {
        List<FamilyLink> links = new ArrayList<>();

        String sql = """
                SELECT *
                FROM family_links
                WHERE elder_id = ?
                ORDER BY created_at
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, elderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    links.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("查詢長者家屬失敗：" + e.getMessage());
        }

        return links;
    }

    public List<FamilyLink> findByFamilyId(int familyId) {
        List<FamilyLink> links = new ArrayList<>();

        String sql = """
                SELECT *
                FROM family_links
                WHERE family_id = ?
                ORDER BY created_at
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, familyId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    links.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("查詢家屬綁定長者失敗：" + e.getMessage());
        }

        return links;
    }

    public boolean exists(int elderId, int familyId) {
        String sql = """
                SELECT 1
                FROM family_links
                WHERE elder_id = ? AND family_id = ?
                """;

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, elderId);
            stmt.setInt(2, familyId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("檢查家屬綁定失敗：" + e.getMessage());
            return false;
        }
    }

    private FamilyLink mapRow(ResultSet rs) throws SQLException {
        return new FamilyLink(
                rs.getInt("id"),
                rs.getInt("elder_id"),
                rs.getInt("family_id"),
                rs.getString("relationship"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}