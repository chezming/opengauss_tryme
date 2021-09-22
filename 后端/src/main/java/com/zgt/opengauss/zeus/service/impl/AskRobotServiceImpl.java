package com.zgt.opengauss.zeus.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zgt.opengauss.zeus.service.AskRobotService;
import com.zgt.opengauss.zeus.util.HttpUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Service
public class AskRobotServiceImpl implements AskRobotService {

    final String TOKEN_URL = "https://iam.cn-north-4.myhuaweicloud.com/v3/auth/tokens";
    final String ROBOT_ENDPOINT = "https://cbs-ext.cn-north-4.myhuaweicloud.com";
    private static final String CHARSET_UTF8 = "UTF-8";

    @Value("robot.projectId")
    private String projectId;

    @Value("robot.qabotId")
    private String qabotId;

    @Value("robot.username")
    private String username;

    @Value("robot.password")
    private String password;


    @Override
    public String getToken() throws Exception{
        String jsonString = getTokenJsonString();

        CloseableHttpResponse response = HttpUtils.doPostJsonForResponse(TOKEN_URL, jsonString, null);
        Header[] tokenHeader = response.getHeaders("X-Subject-Token");
        String token = null;
        if (tokenHeader != null && tokenHeader.length > 0) {
            token = tokenHeader[0].getValue();
        }
        return token;
    }

    @Override
    public String chat(String userId, String question) throws Exception{
        String url = String.format("%s/v1/%s/qabots/%s/chat", ROBOT_ENDPOINT, projectId, qabotId);

        String token = getToken();
        if(token == null){
            return null;
        }
        Map<String, String> headMap = new HashMap<>();
        headMap.put("X-Auth-Token", token);
        String jsonString = getChatJsonString(userId, question);
        String resultString= HttpUtils.doPostJson(url, jsonString, headMap);
        return resultString;
    }

    private String getChatJsonString(String userId, String question ){
        JSONObject obj = new JSONObject();
        obj.put("question", question);
        if(userId != null && !"".equals(userId)){
            obj.put("user_id", userId);
        }
        return  obj.toJSONString();
    }

    private String getTokenJsonString(){
        JSONObject domainObj = new JSONObject();
        domainObj.put("name", username);
        JSONObject userObj = new JSONObject();
        userObj.put("name", username);
        userObj.put("password", password);
        userObj.put("domain", domainObj);
        JSONObject passwordObj = new JSONObject();
        passwordObj.put("user", userObj);
        JSONArray methodsArr = new JSONArray();
        methodsArr.add("password");
        JSONObject identityObj = new JSONObject();
        identityObj.put("methods", methodsArr);
        identityObj.put("password", passwordObj);

        JSONObject projectObj = new JSONObject();
        projectObj.put("name", "cn-north-4" );
        JSONObject scopeObj = new JSONObject();
        scopeObj.put("project", projectObj);

        JSONObject authObj = new JSONObject();
        authObj.put("identity", identityObj);
        authObj.put("scope", scopeObj);

        JSONObject requestObj = new JSONObject();
        requestObj.put("auth", authObj);

        return requestObj.toJSONString();
    }

}
