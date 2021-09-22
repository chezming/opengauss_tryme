package com.zgt.opengauss.zeus;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.zgt.opengauss.zeus.common.SqlType;
import com.zgt.opengauss.zeus.entity.ExecuteResult;
import com.zgt.opengauss.zeus.entity.JobDatasource;
import com.zgt.opengauss.zeus.entity.Result;
import com.zgt.opengauss.zeus.tool.BaseQueryTool;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OpenGaussQueryToolTest {

    private BaseQueryTool queryTool;
    private JobDatasource jdbcDatasource;

    public void before() {
        jdbcDatasource = new JobDatasource();
        jdbcDatasource.setDatasourceName("zgt");
        jdbcDatasource.setJdbcUsername("zgt");
        jdbcDatasource.setJdbcPassword("zgt@123456");
        jdbcDatasource.setJdbcUrl("jdbc:postgresql://122.9.117.144:5432/postgres");
        jdbcDatasource.setJdbcDriverClass("org.postgresql.Driver");
        try {
            queryTool = new BaseQueryTool(jdbcDatasource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTableColumns() {
        before();
        List<ExecuteResult> list = new ArrayList<>();
//        list.add(queryTool.executeSelectSql("INSERT INTO \"zgt\".\"bangtest3\"(\"name\", \"age\", \"caree\") VALUES ('1111111', 1111, '111') RETURNING *;SELECT * FROM sys_user"));
//        list.add(queryTool.executeSelectSql("select version"));
        String sql1 = "DELETE FROM zgt.bangtest WHERE 'name' = 'bang1';DELETE FROM zgt.bangtest WHERE 'name' = 'bang2";
        String sql = "drop function if exists random_int(integer, integer);" +
                "create or replace function random_int(_begin int, _end int) " +
                "  returns int as $$ " +
                "declare " +
                "  res text; " +
                "begin " +
                "  select floor(random() * (_end + 1 - _begin)) + _begin into res; " +
                "  return res; " +
                "end; " +
                "$$ " +
                "language plpgsql " +
                "strict;";
        System.out.println(Result.OK(list));
//        System.out.println(list);
//        list.forEach(System.out::println);
    }

    @Test
    public void aaa() {

        final DbType dbType = JdbcConstants.POSTGRESQL; // 可以是ORACLE、POSTGRESQL、SQLSERVER、ODPS等
        String sql = "SELECT * FROM sys_user;";
        System.out.println(sql);
//        String sql = "drop function if exists random_int(integer, integer);select * from t";
//        String sql = "DELETE FROM \"zgt\".\"bangtest3\" WHERE \"name\" = 'bang00003';" +
//                "select * from t";
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        System.out.println(stmtList);
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
     * 判断语句是什么类型insert select等等
     */
    public ArrayList<String> sqlParse(String sqlStr) {
        String curSql = "";
        String tempArr[] = {};
        SqlType sqlType = SqlType.UNKNOWN;
        ArrayList<String> sqlArr = new ArrayList<String>();
        String strArr[] = sqlStr.split(";");
        for (String sql : strArr) {
            sqlType = GetSqlType(sql, " ");
            if (sqlType == SqlType.COMMENT) {
                continue;
            }

            if (sqlType != SqlType.UNKNOWN ) {
                sqlArr.add(sql+";&"+sqlType.toString());
                curSql = "";
                curSql = curSql + sql + ";";
            } else {
                curSql = curSql + sql + ";";
            }
        }
        sqlArr.add(curSql+"&"+ GetSqlType(curSql, " "));
        return sqlArr;
    }

//    @Test
//    public void bb() {
//        String sql = "SELECT 查看openGauss版本";
//        String sql1 = "select * from test";
//        int i = sql.indexOf(";");
////
////        String sql1 = "drop function if exists random_int(integer, integer);\n" +
////                "create or replace function random_int(_begin int, _end int)\n" +
////                "  returns int as $$\n" +
////                "declare\n" +
////                "  res text;\n" +
////                "begin\n" +
////                "  select floor(random() * (_end + 1 - _begin)) + _begin into res;\n" +
////                "  return res;\n" +
////                "end;\n" +
////                "$$\n" +
////                "language plpgsql\n" +
////                "strict;";
////        List<String> list = new ArrayList<>();
////        String[] ss = sql1.split(";");
////        for (String s : ss) {
////            list.add(s);
////        }
//        System.out.println(sqlParse(sql1));
//    }
//
//    private SqlType sqlParse(String sql){
//
//        if (sql.indexOf(" ") != -1){
//            String upperCaseSql = sql.substring(0,sql.indexOf(" ")).trim().replaceAll("\r|\n", "").toUpperCase();
//            switch (upperCaseSql) {
//                case "INSERT" :
//                case "UPDATE" :
//                case "DELETE" :
//                case "CREATE" :
//                case "ALTER" :
//                case "GRANT" :
//                case "DROP" :
//                case "TRUNCATE" :
//                    return SqlType.UPDATE;
//                case "SELECT" :
//                case "EXPLAIN" :
//                    return SqlType.SELECT;
//                case "--" :
//                    return SqlType.COMMENT;
//                default:
//                    return SqlType.UNKNOWN;
//            }
//        }
//        return null;
//    }
}