package com.zgt.opengauss.zeus.service;

import com.zgt.opengauss.zeus.entity.ExecuteCode;
import com.zgt.opengauss.zeus.entity.Result;

import java.sql.SQLException;

/**
 * @author zhangrb
 * @date 2021/6/25
 */
public interface ActuatorService {

    Result<Object> executeSql(ExecuteCode executeCode) throws SQLException;
    Result<Object> executeSql2(ExecuteCode executeCode) throws SQLException;
}
