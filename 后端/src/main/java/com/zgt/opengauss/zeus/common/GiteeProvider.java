package com.zgt.opengauss.zeus.common;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zgt.opengauss.zeus.entity.UserDTO;
import com.zgt.opengauss.zeus.util.HttpUtils;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 根据code获取access_token，在哪access_token换取用户信息
 */
@Component
@Slf4j
public class GiteeProvider {
    private final String GET_ACCESS_TOKEN_URL = "https://gitee.com/oauth/token?grant_type=authorization_code&code={}&client_id={}&redirect_uri={}&client_secret={}";
    private final String GET_USER_INFO_URL = "https://gitee.com/api/v5/user?access_token={}";

    @Value("${gitee.redirect.uri}")
    private String redirectUri;
    @Value("${gitee.client.cid}")
    private String clientId;
    @Value("${gitee.client.secret}")
    private String clientSecret;

    /**
     * 获取用户信息
     *
     * @param code
     * @return
     */
    public UserDTO getUserInfo(String code) {
        String url = StrUtil.format(GET_ACCESS_TOKEN_URL, code, clientId, redirectUri, clientSecret);
        String respData = HttpUtils.doPost(url, new HashMap<>());
        System.out.println("Git 授权码登录返回结果--------------------------------------\n"+respData);
        String accessToken = JSON.parseObject(respData).getString("access_token");
        if (!StringUtil.isNullOrEmpty(accessToken)){
            String userInfoStr = HttpUtils.doGet(StrUtil.format(GET_USER_INFO_URL, accessToken));
            // fastJson可以自动将下划线转驼峰，例如avatar_url可以映射为avatarUrl或者avatarurl
            UserDTO userDTO = JSON.parseObject(userInfoStr, UserDTO.class);
            return userDTO;
        }
        return null;
    }
}
