package com.zgt.opengauss.zeus.service;

/**
 * 华为问答机器人服务
 */
public interface AskRobotService {
    String getToken() throws Exception;  // 获取token

    String chat(String userId, String question) throws Exception;
}
