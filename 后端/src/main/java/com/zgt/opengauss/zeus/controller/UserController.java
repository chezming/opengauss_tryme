package com.zgt.opengauss.zeus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgt.opengauss.zeus.annotation.PassToken;
import com.zgt.opengauss.zeus.entity.*;
import com.zgt.opengauss.zeus.service.SysApplyService;
import com.zgt.opengauss.zeus.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangrb
 * @date 2021/6/29
 */
@RequestMapping("/user")
@RestController
@Api(tags = "用户接口")
public class UserController {

    @Autowired
    private SysApplyService sysApplyService;
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "admin审批列表", notes = "管理员查看审批列表")
    @GetMapping("/apply/list")
    @PassToken
    public Result<Object> list(Page page){

        try {
            QueryWrapper<SysApply> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("*");
            queryWrapper.eq("status","0");
            return Result.OK(sysApplyService.page(page,queryWrapper));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "我的申请", notes = "根据用户gitId查询申请")
    @GetMapping("/apply/{gitId}")
    @PassToken
    public Result<Object> myList(@PathVariable String gitId){
        try {
            QueryWrapper<SysApply> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("*");
            queryWrapper.eq("git_id",gitId);
            queryWrapper.last("LIMIT 1");
            return Result.OK(sysApplyService.getOne(queryWrapper));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "用户被驳回重新申请")
    @PostMapping("/reApply")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="申请id",required=true),
            @ApiImplicitParam(name="applyReason",value="申请理由",required=true)
    })
    @PassToken
    public Result<Object> reApply(@RequestParam("id") String id, @RequestParam("applyReason") String applyReason){

        try {
            UpdateWrapper<SysApply> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id);
            updateWrapper.set("apply_reason",applyReason);
            updateWrapper.set("status","0");
            sysApplyService.update(updateWrapper);
            return Result.OK();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }


    @ApiOperation(value = "用户申请管理员权限")
    @PostMapping("/apply")
    public Result<Object> applySave(@RequestBody SysApplyVO sysApplyVO){

        try {
            sysApplyService.saveApply(sysApplyVO);
            return Result.OK();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "删除我的申请")
    @GetMapping("/apply/del")
    public Result<Object> applyDel(@RequestParam("id") String id){

        try {
            sysApplyService.removeById(id);
            return Result.OK();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "权限申请审批")
    @PostMapping("/approval")
    @PassToken
    public Result<Object> approval(@RequestBody SysApprovalVO sysApprovalVO){

        try {
            sysApplyService.approval(sysApprovalVO);
            return Result.OK();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "删除我的Docker数据库空间")
    @GetMapping("/docker/del")
    public Result<Object> dockerDel(@RequestParam("gitId") String gitId){

        try {
            if (sysUserService.deleteDockerById(gitId)) {
                return Result.OK();
            }
            return Result.error("删除失败");
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "创建我的试用数据库空间")
    @GetMapping("/docker/create")
    public Result<Object> dockerCreate(@RequestParam("gitId") String gitId){

        try {
            if (sysUserService.CreateDockerById(gitId)) {
                return Result.OK();
            }
            return Result.error("创建失败");
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "获取我的截止时间戳")
    @GetMapping("/docker/time")
    public Result<Object> dockerTime(@RequestParam("gitId") String gitId) {

        try {
            SysUser sysUser = sysUserService.getById(gitId);
            if (sysUser!=null) {
                return Result.OK(sysUser.getValidTime()==null ? null : sysUser.getValidTime().getTime());
            }
            return Result.error("获取时间失败");
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

}
