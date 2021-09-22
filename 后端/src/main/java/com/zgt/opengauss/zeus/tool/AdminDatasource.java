package com.zgt.opengauss.zeus.tool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhangrb
 * @date 2021/6/29
 */
@ConfigurationProperties(prefix = "opengauss.super")
@Component
@Data
public class AdminDatasource {

    private String url;

    private String datasourceName;

    private String username;

    private String password;

    // 配置文件中是driver-class-name, 转驼峰命名便可以绑定成
    private String driverClassName;

    private String type;

    private String dockerImage;

}
