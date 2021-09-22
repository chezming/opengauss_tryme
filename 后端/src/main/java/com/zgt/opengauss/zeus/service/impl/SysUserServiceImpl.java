package com.zgt.opengauss.zeus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.zgt.opengauss.zeus.entity.LoginUser;
import com.zgt.opengauss.zeus.entity.SysPort;
import com.zgt.opengauss.zeus.entity.SysUser;
import com.zgt.opengauss.zeus.mapper.SysUserMapper;
import com.zgt.opengauss.zeus.service.SysPortService;
import com.zgt.opengauss.zeus.service.SysUserService;
import com.zgt.opengauss.zeus.tool.AdminDatasource;
import com.zgt.opengauss.zeus.util.DateUtil;
import com.zgt.opengauss.zeus.util.DockerClientUtil;
import com.zgt.opengauss.zeus.util.oConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zhangrb
 * @date 2021/6/29
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysPortService sysPortService;
    @Autowired
    private AdminDatasource adminDatasource;
    @Value("${docker.url}")
    private String dockerUrl;

    @Override
    public LoginUser getUserByName(String username) {

        if(oConvertUtils.isEmpty(username)) {
            return null;
        }
        LoginUser loginUser = new LoginUser();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*");
        queryWrapper.eq("username", username);
        SysUser sysUser = baseMapper.selectOne(queryWrapper);
        if(sysUser==null) {
            return null;
        }
        BeanUtils.copyProperties(sysUser, loginUser);
        return loginUser;
    }

    @Override
    public int updateUserRole(String gitId, String role, Date validTime) {
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        SysUser sysUser = new SysUser();
        sysUser.setRole(role);
        sysUser.setValidTime(validTime);
        updateWrapper.eq("id", gitId);
        return baseMapper.update(sysUser,updateWrapper);
    }

    @Override
    public Boolean deleteDockerById(String gitId) {

        SysUser user = baseMapper.selectById(gitId);
        if (user != null && StringUtils.isNotBlank(user.getContainerId())) {
            DockerClientUtil dockerClientUtil = new DockerClientUtil();
            // 连接获得client对象
            DockerClient client = dockerClientUtil.connectDocker(dockerUrl);
            dockerClientUtil.stopContainer(client,user.getContainerId());
            dockerClientUtil.removeContainer(client,user.getContainerId());
            user.setPort("");
            user.setValidTime(new Date());
            user.setContainerId("");
            this.saveOrUpdate(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean CreateDockerById(String gitId) {
        //获取用户信息
        SysUser querySysUser = baseMapper.selectById(gitId);
        if (querySysUser != null) {
            if (StringUtils.isBlank(querySysUser.getPort()) || querySysUser.getValidTime().getTime() < new Date().getTime()) {
                createDockerByUser(querySysUser);
            }
        } else {
            return false;
        }
        return true;
    }

    private void createDockerByUser(SysUser sysUser) {
        DockerClientUtil dockerClientUtil = new DockerClientUtil();
        // 连接获得client对象
        DockerClient client = dockerClientUtil.connectDocker(dockerUrl);
        SysPort sysPort = sysPortService.getById(1);
        Integer port = sysPort.getPort();
        // 创建容器
        CreateContainerResponse container = dockerClientUtil.createContainers(client,adminDatasource.getDockerImage(),port,sysUser.getGitId(),adminDatasource.getPassword());
        // 开始容器
        client.startContainerCmd(container.getId()).exec();
        sysUser.setPort(String.valueOf(port));
        sysUser.setContainerId(container.getId());
        sysUser.setValidTime(DateUtil.getTimeAddDay(2));
        sysPort.setPort(port+1);
        sysPortService.updateById(sysPort);
        this.saveOrUpdate(sysUser);
    }
}
