package com.zgt.opengauss.zeus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.zgt.opengauss.zeus.common.CommonConstant;
import com.zgt.opengauss.zeus.common.GiteeProvider;
import com.zgt.opengauss.zeus.entity.Result;
import com.zgt.opengauss.zeus.entity.SysPort;
import com.zgt.opengauss.zeus.entity.SysUser;
import com.zgt.opengauss.zeus.entity.UserDTO;
import com.zgt.opengauss.zeus.service.GiteeService;
import com.zgt.opengauss.zeus.service.SysPortService;
import com.zgt.opengauss.zeus.service.SysUserService;
import com.zgt.opengauss.zeus.tool.AdminDatasource;
import com.zgt.opengauss.zeus.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @author zhangrb
 * @date 2021/6/25
 */
@Service
public class GiteeServiceImpl implements GiteeService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private GiteeProvider giteeProvider;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPortService sysPortService;
    @Autowired
    private AdminDatasource adminDatasource;

    @Override
    public Result<Object> loginByGit(String code) {

        UserDTO userDTO = giteeProvider.getUserInfo(code);
        if (userDTO == null) {
            return Result.error(500, "gitee 认证授权登录失败");
        }

        return Result.OK(generateToken(userDTO));
    }

    @Override
    public Result<Object> loginByUser(String gitId, String login) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(gitId);
        userDTO.setLogin(login);
        userDTO.setName(login);
        userDTO.setAvatarUrl("http://localhost/test.png");
        userDTO.setBio("bio");

        return Result.OK(generateToken(userDTO));
    }

    private UserDTO generateToken(UserDTO userDTO) {

        //生成token信息
        String userName = userDTO.getId() + userDTO.getLogin();
        String passWord = UUID.randomUUID().toString().replaceAll("-", "");
        String token = JwtUtil.sign(userName, passWord);
        System.out.println("token:" + token);
        //设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.DAY_TIME * 2 / 1000);
//        redisUtil.persist(CommonConstant.PREFIX_USER_TOKEN + token);
        userDTO.setToken(token);

        //保存用户
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDTO, sysUser);
        sysUser.setGitId(userDTO.getId());
        sysUser.setUsername(userName);
        String salt = oConvertUtils.randomGen(8);
        sysUser.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(userName, passWord, salt);
        sysUser.setPassword(passwordEncode);
        //获取用户信息
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "git_id","login","role_code","valid_time");
        queryWrapper.eq("git_id", userDTO.getId());
        SysUser querySysUser = sysUserService.getOne(queryWrapper);
        if (querySysUser == null) {
            sysUser.setDelFlag("0");
            sysUser.setRole("0");
            sysUser.setRoleCode("ROLE_NORMAL");
            sysUserService.save(sysUser);
            userDTO.setRoleCode("ROLE_NORMAL");
        } else {
            sysUserService.update(sysUser,queryWrapper);
            userDTO.setRoleCode(querySysUser.getRoleCode());
        }
        return userDTO;
    }

}
