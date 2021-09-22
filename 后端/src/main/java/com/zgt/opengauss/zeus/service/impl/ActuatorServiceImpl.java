package com.zgt.opengauss.zeus.service.impl;

import com.zgt.opengauss.zeus.common.FailException;
import com.zgt.opengauss.zeus.common.SqlType;
import com.zgt.opengauss.zeus.entity.*;
import com.zgt.opengauss.zeus.service.ActuatorService;
import com.zgt.opengauss.zeus.service.SysUserService;
import com.zgt.opengauss.zeus.tool.AdminDatasource;
import com.zgt.opengauss.zeus.tool.BaseQueryTool;
import com.zgt.opengauss.zeus.tool.CommonDatasource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

/**
 * @author zhangrb
 * @date 2021/6/25
 */
@Service
public class ActuatorServiceImpl implements ActuatorService {

    @Autowired
    private CommonDatasource commonDatasource;
    @Autowired
    private AdminDatasource adminDatasource;

    @Autowired
    private SysUserService userService;

    @Override
    public Result<Object> executeSql(ExecuteCode executeCode) throws SQLException {

        List<ExecuteResult> list = new ArrayList<>();
        BaseQueryTool queryTool;
        if (executeCode.getSimple() == 1) {
            queryTool = new BaseQueryTool(getDatasource());
        } else {
            if (getDockerDatasource(executeCode.getGitId(),executeCode.getDbName()) == null) {
                return Result.error("试用时间到,数据将试用期结束在三小时后销毁。");
            }
            queryTool = new BaseQueryTool(getDockerDatasource(executeCode.getGitId(),executeCode.getDbName()));
        }

        //截取语句,判断语句执行类型 select insert 等等
//        String[] sqls = executeCode.getCode().split(";");
        ArrayList<String>  sqls = sqlParse(executeCode.getCode());
        for (int i = 0;i < sqls.size();i++) {
            String temp[] = sqls.get(i).split("&");
            String sql = temp[0];
            switch (Enum.valueOf(SqlType.class,temp[1])){
                //insert delete update create
                case UPDATE:
                    list.add(queryTool.executeUpdateSql(sql));
                    break;
                case SELECT:
                    list.add(queryTool.executeSelectSql(sql));
                    break;
                case COMMENT:
                    break;
                case UNKNOWN:
                    list.add(new ExecuteResult(sql,"暂不支持该SQL"));
                    break;
                default:
                    list.add(new ExecuteResult(sql,"语句解析失败"));
            }
        }
        return Result.OK(list);
    }

    @Override
    public Result<Object> executeSql2(ExecuteCode executeCode) throws SQLException {
        if (getDockerDatasource(executeCode.getGitId(),executeCode.getDbName()) == null) {
            return Result.error("试用时间到,数据将试用期结束在三小时后销毁。");
        }
        BaseQueryTool queryTool = new BaseQueryTool(getDockerDatasource(executeCode.getGitId(),executeCode.getDbName()));
        return Result.OK(queryTool.executeSelectSql2(executeCode.getCode()));
    }


    /**
     * 判断用户是否有管理员权限
     * 返回数据源
     * @return
     */
    private JobDatasource getDatasource(){
        JobDatasource jdbcDatasource = new JobDatasource();
//        SysUser sysUser = userService.getById(gitId);
        //公共数据源
        jdbcDatasource.setDatasourceName(commonDatasource.getDatasourceName());
        jdbcDatasource.setJdbcUsername(commonDatasource.getUsername());
        jdbcDatasource.setJdbcPassword(commonDatasource.getPassword());
        jdbcDatasource.setJdbcUrl(commonDatasource.getUrl());
        jdbcDatasource.setJdbcDriverClass(commonDatasource.getDriverClassName());
//        if (sysUser != null){
//            if ("1".equals(sysUser.getRole()) && sysUser.getValidTime().getTime() > new Date().getTime()) {
//                //管理员数据源
//                jdbcDatasource.setDatasourceName(adminDatasource.getDatasourceName());
//                jdbcDatasource.setJdbcUsername(adminDatasource.getUsername());
//                jdbcDatasource.setJdbcPassword(adminDatasource.getPassword());
//                jdbcDatasource.setJdbcUrl(adminDatasource.getUrl());
//                jdbcDatasource.setJdbcDriverClass(adminDatasource.getDriverClassName());
//            }
//        }
        return jdbcDatasource;
    }
    /**
     * 每个用户使用独立的docker数据源
     *
     * 返回数据源
     * @param gitId
     * @return
     */
    private JobDatasource getDockerDatasource(String gitId,String dbName){
        JobDatasource jdbcDatasource = new JobDatasource();
        SysUser sysUser = userService.getById(gitId);
        if (sysUser != null){
            if (StringUtils.isNotBlank(sysUser.getPort()) && sysUser.getValidTime().getTime() > new Date().getTime()) {
                //管理员数据源
                jdbcDatasource.setDatasourceName(adminDatasource.getDatasourceName());
                jdbcDatasource.setJdbcUsername(adminDatasource.getUsername());
                jdbcDatasource.setJdbcPassword(adminDatasource.getPassword());
                String newUrl = adminDatasource.getUrl().replace("5432",sysUser.getPort());
                jdbcDatasource.setJdbcUrl(newUrl);
                if (StringUtils.isNotBlank(dbName)) {
                    jdbcDatasource.setDatasourceName(adminDatasource.getDatasourceName() + dbName);
                    jdbcDatasource.setJdbcUrl(newUrl.replace("/postgres","/"+dbName));
                }
                jdbcDatasource.setJdbcDriverClass(adminDatasource.getDriverClassName());
            } else {
                return null;
            }
            return jdbcDatasource;
        }
        return null;
    }

    private SqlType  GetSqlType(String sql, String gap) {
        int iPos = sql.indexOf(gap);
        String sqlType = "";
        if (iPos != -1) {
            sqlType = sql.substring(0, iPos).trim().replaceAll("\r|\n", "").toUpperCase();
            switch (sqlType) {
                case "INSERT" :
                case "UPDATE" :
                case "DELETE" :
                case "CREATE" :
                case "ALTER" :
                case "GRANT" :
                case "DROP" :
                case "TRUNCATE" :
                    return SqlType.UPDATE;
                case "SELECT" :
                case "EXPLAIN" :
                    return SqlType.SELECT;
                case "--" :
                    return SqlType.COMMENT;
                default:
                    return SqlType.UNKNOWN;
            }
        }
        return SqlType.UNKNOWN;
    }

    /**
     * luowq  解析sql，拼接语句
     */
    public ArrayList<String> sqlParse(String sqlStr) {
        String curSql = "";
        String tempArr[] = {};
        SqlType sqlType = SqlType.UNKNOWN;
        ArrayList<String> sqlArr = new ArrayList<String>();
        String strArr[] = sqlStr.split(";");
        for (int i =0;i < strArr.length;i++) {
            sqlType = GetSqlType(strArr[i], " ");
            if (sqlType == SqlType.COMMENT) {
                continue;
            }

            if (sqlType != SqlType.UNKNOWN ) {
                if(i+1 >= strArr.length )
                {
                    sqlArr.add(strArr[i].replace("\r\n","") + ";&" + GetSqlType(strArr[i], " "));
                }
                else{
                    SqlType temp = GetSqlType(strArr[i+1], " ");
                    if(temp != SqlType.UNKNOWN )
                    {
                        if(curSql.isEmpty()) {
                            sqlArr.add(strArr[i].replace("\r\n","") + ";&" + GetSqlType(strArr[i], " "));
                        }
                        else {
                            sqlArr.add(curSql  + "&" + GetSqlType(curSql, " "));
                            curSql="";
                        }
                    }
                    else{
                        curSql = curSql + strArr[i] + ";";
                    }
                }
            } else {
                curSql = curSql + strArr[i] + ";";
            }
        }
        if(!curSql.isEmpty())
        {
            sqlArr.add(curSql+"&"+ GetSqlType(curSql, " "));
        }
        return sqlArr;
    }
}
