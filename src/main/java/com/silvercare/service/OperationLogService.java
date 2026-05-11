package com.silvercare.service;

import com.silvercare.dao.OperationLogDAO;
import com.silvercare.model.OperationLog;
import com.silvercare.model.enums.ActionType;
import com.silvercare.model.enums.TargetType;

/**
 * 操作紀錄 Service
 */
public class OperationLogService {

    private final OperationLogDAO
            operationLogDAO =
            new OperationLogDAO();

    /**
     * 記錄操作
     */
    public void log(
            int userId,
            ActionType actionType,
            TargetType targetType,
            int targetId
    ) {

        OperationLog log =
                new OperationLog(
                        userId,
                        actionType,
                        targetType,
                        targetId
                );

        operationLogDAO.create(log);
    }
}