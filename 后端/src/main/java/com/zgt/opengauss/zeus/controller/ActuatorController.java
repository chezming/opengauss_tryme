package com.zgt.opengauss.zeus.controller;

import com.zgt.opengauss.zeus.annotation.PassToken;
import com.zgt.opengauss.zeus.annotation.UserLoginToken;
import com.zgt.opengauss.zeus.entity.ExecuteCode;
import com.zgt.opengauss.zeus.entity.LoginUser;
import com.zgt.opengauss.zeus.entity.QueryInfo;
import com.zgt.opengauss.zeus.entity.Result;
import com.zgt.opengauss.zeus.service.ActuatorService;
import com.zgt.opengauss.zeus.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @author zhangrb
 * @date 2021/6/25
 */
@Api(tags = "OpenGauss SQL语句执行器")
@RequestMapping("/actuator")
@RestController
public class ActuatorController {

    @Autowired
    private ActuatorService service;
    @Autowired
    private SysUserService sysUserService;


    @ApiOperation(value = "SQL执行接口")
    @PostMapping("/code")
    @PassToken
    public Result<Object> executeSQL(@RequestBody ExecuteCode executeCode){

        try {
            return service.executeSql(executeCode);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "需要token校验的接口")
    @GetMapping("/test")
    @UserLoginToken
    public Result test(){
        return Result.OK();
    }
}
