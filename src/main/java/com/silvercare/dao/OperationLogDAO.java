package com.silvercare.dao;

import com.silvercare.config.DatabaseConfig;
import com.silvercare.model.OperationLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 操作紀錄 DAO
 */
public class OperationLogDAO {

    /**
     * 新增操作紀錄
     */
    public boolean create(
            OperationLog log
    ) {

        String sql = """
                INSERT INTO operation_logs
                (
                    user_id,
                    action_type,
                    target_type,
                    target_id
                )
                VALUES (?, ?, ?, ?)
                """;

        try (

                Connection conn =
                        DatabaseConfig
                                .getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)

        ) {

            ps.setInt(
                    1,
                    log.getUserId()
            );

            ps.setString(
                    2,
                    log.getActionType().name()
            );

            ps.setString(
                    3,
                    log.getTargetType().name()
            );

            ps.setInt(
                    4,
                    log.getTargetId()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "新增操作紀錄失敗："
                            + e.getMessage()
            );
        }

        return false;
    }
}