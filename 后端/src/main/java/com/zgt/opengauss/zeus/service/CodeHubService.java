package com.zgt.opengauss.zeus.service;


import com.zgt.opengauss.zeus.entity.CodeHub;
import com.zgt.opengauss.zeus.entity.Result;


/**
 * @author luowq
 * @date 2021/7/7
 */
public interface CodeHubService {
    Result<Object> saveOrUpdateCodehub(CodeHub codehub);
    Result<Object> getCodehubByGitId(String gitId,int delFlag,int pageNum);
    Result<Object> getCodehubById(String  id);
    Result<Object> deleteCodehubById(String id);
}
