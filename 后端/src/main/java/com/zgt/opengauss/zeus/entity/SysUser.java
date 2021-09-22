package com.zgt.opengauss.zeus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhangrb
 * @date 2021/6/29
 */
@Data
public class SysUser {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;
    @ApiModelProperty(value = "账号状态")
    private String delFlag;
    @ApiModelProperty(value = "角色")
    private String role;
    @ApiModelProperty(value = "角色标识 ROLE_ADMIN —— 有审批权限的管理员  ROLE_NORMAL —— 有申请权限的普通用户")
    private String roleCode;
    @ApiModelProperty(value = "端口")
    private String port;
    @ApiModelProperty(value = "容器id")
    private String containerId;
    @ApiModelProperty(value = "角色有效时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validTime;
}
