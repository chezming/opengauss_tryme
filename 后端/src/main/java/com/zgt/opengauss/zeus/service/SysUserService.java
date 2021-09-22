package com.zgt.opengauss.zeus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgt.opengauss.zeus.entity.LoginUser;
import com.zgt.opengauss.zeus.entity.SysUser;

import java.util.Date;

/**
 * @author zhangrb
 * @date 2021/6/29
 */
public interface SysUserService extends IService<SysUser> {

    LoginUser getUserByName(String username);

    int updateUserRole(String gitId, String role, Date validTime);

    Boolean deleteDockerById(String gitId);

    boolean CreateDockerById(String gitId);
}
