package com.zgt.opengauss.zeus.controller;

import com.zgt.opengauss.zeus.entity.Result;
import com.zgt.opengauss.zeus.service.GiteeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangrb
 * @date 2021/6/24
 */
@Api(tags = "git用户认证登录")
@RestController
@RequestMapping("/git")
public class GiteeController {


    @Autowired
    private GiteeService service;

    @ApiOperation(value = "GIt登录", notes = "根据code登录并获取用户信息")
    @GetMapping("/login")
    public Result<Object> login(@RequestParam("code") String code) {

        return service.loginByGit(code);
    }

    @ApiOperation(value = "普通登录", notes = "根据gitId git登陆名来登录并获取用户信息")
    @GetMapping("/common/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gitId",value="git 用户id",required=true),
            @ApiImplicitParam(name="login",value="git 用户英文登录名",required=true)
    })
    public Result<Object> commonLogin(@RequestParam("gitId") String gitId, @RequestParam("login") String login) {

        return service.loginByUser(gitId,login);
    }

}
