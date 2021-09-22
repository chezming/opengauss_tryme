package com.zgt.opengauss.zeus.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangrb
 * @date 2021/6/24
 */
@Data
public class UserDTO {
    @ApiModelProperty(value = "git ID")
    private String id;
    private String login;
    private String name;
    private String avatarUrl;
    private String bio;
    private String token;
    private String roleCode;
}
