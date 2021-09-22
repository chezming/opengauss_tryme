package com.zgt.opengauss.zeus.controller;

import com.zgt.opengauss.zeus.annotation.PassToken;
import com.zgt.opengauss.zeus.entity.ExecuteCode;
import com.zgt.opengauss.zeus.entity.QueryInfo;
import com.zgt.opengauss.zeus.entity.Result;
import com.zgt.opengauss.zeus.service.ActuatorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @author luowq
 * @date 2021/8/30
 */
@Api(tags="数据库信息列表查询")
@RestController
@RequestMapping("/infos")
public class QueryInfoController {
    @Autowired
    private ActuatorService service;

    @ApiOperation(value="数据库列表查询",notes = "根据gitid获取用户对应的数据库列表")
    @PostMapping("/dats")
    @PassToken
    public Result<Object> getDatabases(@RequestBody QueryInfo queryInfo) throws SQLException {
        ExecuteCode executeCode = new ExecuteCode();
        executeCode.setGitId(queryInfo.getGitId());
        executeCode.setDbName(queryInfo.getDatName());
        switch (queryInfo.getQueryType())
        {
            //获取数据库列表
            case 0:
                executeCode.setCode("select datname from pg_database where datname not ILIKE '%temp%'");
                break;
            //获取模式
            case 1:
                executeCode.setCode(String.format("select schema_name from information_schema.schemata where " +
                        "catalog_name='%s'",queryInfo.getDatName()));
                break;
            //获取数据表列表
            case 2:
                executeCode.setCode(String.format("select tablename from pg_tables where schemaname='%s'",queryInfo.getSchemaName()));
                break;
            //获取视图列表
            case 3:
                executeCode.setCode(String.format("SELECT viewname FROM pg_views WHERE schemaname ='%s'",queryInfo.getSchemaName()));
                break;
            //获取函数列表
            case 4:
                executeCode.setCode(String.format("select routine_name  from information_schema.routines where routine_catalog='%s' " +
                        "and routine_schema='%s'order by routine_name;",queryInfo.getDatName(),queryInfo.getSchemaName()));
                break;
            default:break;
        }

        return service.executeSql2(executeCode);
    }
}
