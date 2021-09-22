//package com.zgt.opengauss.zeus.tool;
//
//import com.zgt.opengauss.zeus.entity.JobDatasource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.sql.SQLException;
//
///**
// * @author zhangrb
// * @date 2021/7/14
// */
//@Component
//public class ApplicationRunnerImpl implements ApplicationRunner {
//
//    @Autowired
//    private CommonDatasource commonDatasource;
//    @Autowired
//    private AdminDatasource adminDatasource;
//
//    @Override
//    public void run(ApplicationArguments args) throws SQLException {
//        System.out.println("通过实现ApplicationRunner接口，初始化JDBC连接");
//        //初始化公共数据源连接
//        init(commonDatasource.getDatasourceName(), commonDatasource.getUsername(),
//                commonDatasource.getPassword(), commonDatasource.getUrl(), commonDatasource.getDriverClassName());
//        //初始化管理员数据源连接
//        init(adminDatasource.getDatasourceName(), adminDatasource.getUsername(),
//                adminDatasource.getPassword(), adminDatasource.getUrl(), adminDatasource.getDriverClassName());
//        return;
//    }
//
//    private void init(String datasourceName, String username, String password, String url, String driverClassName) throws SQLException {
//        JobDatasource adminJdbcDatasource = new JobDatasource();
//        adminJdbcDatasource.setDatasourceName(datasourceName);
//        adminJdbcDatasource.setJdbcUsername(username);
//        adminJdbcDatasource.setJdbcPassword(password);
//        adminJdbcDatasource.setJdbcUrl(url);
//        adminJdbcDatasource.setJdbcDriverClass(driverClassName);
//        new BaseQueryTool(adminJdbcDatasource);
//    }
//
//}
