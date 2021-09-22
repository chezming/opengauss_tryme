package com.zgt.opengauss.zeus;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.zgt.opengauss.zeus.entity.UserDTO;
import com.zgt.opengauss.zeus.util.DateUtil;
import com.zgt.opengauss.zeus.util.DockerClientUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author zhangrb
 * @date 2021/8/30
 */
public class DockerTest {

    @Test
    public void createDocker() {
        String name = "enmotech/opengauss:latest";
        DockerClientUtil dockerClientUtil = new DockerClientUtil();
        // 连接获得client对象
        DockerClient client = dockerClientUtil.connectDocker("tcp://121.36.12.135:2375");
        // 创建容器
        CreateContainerResponse container = dockerClientUtil.createContainers(client,name,30005,"6666666","Zgt@123456");
        // 开始容器
        client.startContainerCmd(container.getId()).exec();
    }

    @Test
    public void startDocker() {
        DockerClientUtil dockerClientUtil = new DockerClientUtil();
        // 连接获得client对象
        DockerClient client = dockerClientUtil.connectDocker("");
        // 开始容器
        dockerClientUtil.startContainer(client,"396299b2e0c445545a49da5fd3a4bb33ce6c6954049e78cfd22365e819be4ab9");
    }


    @Test
    public void aaa() {

        UserDTO userDTO = new UserDTO();
        userDTO.setId("111");
        userDTO.setAvatarUrl("http://localhost/test.png");
        userDTO.setBio("bio");
        eee(userDTO);
        System.out.println(userDTO);

    }

    private void eee(UserDTO userDTO) {
        userDTO.setName("张三");
        userDTO.setLogin("zhangsna");
    }


}
