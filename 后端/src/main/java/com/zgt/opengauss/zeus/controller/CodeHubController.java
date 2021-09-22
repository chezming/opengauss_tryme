package com.zgt.opengauss.zeus.controller;


import com.zgt.opengauss.zeus.annotation.PassToken;
import com.zgt.opengauss.zeus.entity.CodeHub;
import com.zgt.opengauss.zeus.entity.Result;
import com.zgt.opengauss.zeus.service.CodeHubService;
import com.zgt.opengauss.zeus.util.oConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
/**
 * @author luowq
 * @date 2021/7/6
 */
@Api(tags = "OpenGauss SQL代码仓库")
@RequestMapping("/codehub")
@RestController
public class CodeHubController {

    @Autowired
    private CodeHubService service;

    @ApiOperation(value = "保存接口")
    @PostMapping("/saveorupdate")
    @PassToken
    public Result<Object> saveCodehub(@RequestBody CodeHub codeHub){
        try {
            if(oConvertUtils.isEmpty(codeHub.getId())){
                codeHub.setId(UUID.randomUUID().toString().replaceAll("-",""));
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            codeHub.setUpdateTime(simpleDateFormat.format(new Date()));
            return service.saveOrUpdateCodehub(codeHub);
        }catch (Exception e){
            if(e instanceof DuplicateKeyException)
            {
                return Result.error("不能保存同名的代码！");
            }
            else {
                e.printStackTrace();
                return Result.error(e.getMessage());
            }
        }
    }

    @ApiOperation(value = "通过gitid查询代码仓库")
    @GetMapping("/query/git")
    @PassToken
    public Result<Object> queryCodeHubByGitId(@RequestParam("gitId") String gitId,
            @RequestParam(value = "delFlag",defaultValue = "0") int delFlag, @RequestParam("pageNum") int pageNum){
        try{
          return service.getCodehubByGitId(gitId,delFlag,pageNum) ;
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "单条代码查询")
    @GetMapping("/query")
    @PassToken
    public Result<Object> queryCodeHubById(@RequestParam("id") String  id){
        try{
            return service.getCodehubById(id) ;
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "删除单条记录")
    @PostMapping("/delete")
    @PassToken
    public Result<Object> deleteCodeHub(@RequestParam("id") String  id){
        try{
            return service.deleteCodehubById(id) ;
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}
