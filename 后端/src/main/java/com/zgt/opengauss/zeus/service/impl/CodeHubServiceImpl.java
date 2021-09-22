package com.zgt.opengauss.zeus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgt.opengauss.zeus.entity.Result;
import com.zgt.opengauss.zeus.mapper.CodeHubMapper;
import com.zgt.opengauss.zeus.service.CodeHubService;
import org.springframework.stereotype.Service;
import com.zgt.opengauss.zeus.entity.CodeHub;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luowq
 * @date 2021/7/6
 */
@Service
public class CodeHubServiceImpl extends ServiceImpl<CodeHubMapper,CodeHub> implements CodeHubService {

    @Resource
    private  CodeHubMapper codeHubMapper;

    @Override
    public Result<Object>  saveOrUpdateCodehub(CodeHub codehub){
        this.saveOrUpdate(codehub);
        return Result.OK();
    }

    @Override
    public  Result<Object> getCodehubByGitId(String gitId,int delFlag,int pageNum){
        Page<CodeHub> codeHubPage = new Page<>(pageNum , 20);
        QueryWrapper<CodeHub> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*");
        queryWrapper.eq("git_id",gitId);
        queryWrapper.eq("del_flag",delFlag);
        int Total = codeHubMapper.selectCount(queryWrapper);
        queryWrapper.groupBy("id","create_time").orderByDesc("create_time");
        IPage<CodeHub> codeHubIPage = codeHubMapper.selectPage(codeHubPage,queryWrapper);
        List<CodeHub>  codeHubList = codeHubIPage.getRecords();
        Map<String, Object> dict = new HashMap<String, Object>();
        dict.put("total",Total);
        dict.put("size",20);
        dict.put("pageNum",pageNum);
        dict.put("data",codeHubList);
        return Result.OK(dict);
    }

    @Override
    public  Result<Object> getCodehubById(String  id){
        QueryWrapper<CodeHub> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*");
        queryWrapper.eq("id",id);
        CodeHub codeHub = codeHubMapper.selectOne(queryWrapper);
        return Result.OK(codeHub);
    }

    @Override
    public  Result<Object> deleteCodehubById(String id){
        CodeHub codehub = new CodeHub();
        codehub.setId(id);
        codehub.setDelFlag(1);
        codeHubMapper.updateById(codehub);
        return Result.OK();
    }

}
