package com.zgt.opengauss.zeus.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author zhangrb
 * @date 2021/6/25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LoginUser {

    /**
     * 登录人id
     */
    private String id;
    @ApiModelProperty(value = "git用户id")
    private String gitId;
    @ApiModelProperty(value = "git登录名")
    private String login;
    @ApiModelProperty(value = "git用户名")
    private String name;
    @ApiModelProperty(value = "系统用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "盐")
    private String salt;
    @ApiModelProperty(value = "头像地址")
    private String avatarUrl;
    @ApiModelProperty(value = "个性签名")
    private String bio;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private String updateTime;
    @ApiModelProperty(value = "账号状态")
    private String delFlag;

}
