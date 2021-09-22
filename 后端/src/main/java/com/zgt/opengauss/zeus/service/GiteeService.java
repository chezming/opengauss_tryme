package com.zgt.opengauss.zeus.service;

import com.zgt.opengauss.zeus.entity.Result;

/**
 * @author zhangrb
 * @date 2021/6/25
 */
public interface GiteeService {

    Result<Object> loginByGit(String code);

    Result<Object> loginByUser(String gitId,String login);

}
