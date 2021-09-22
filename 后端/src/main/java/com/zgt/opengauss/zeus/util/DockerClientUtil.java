package com.zgt.opengauss.zeus.util;

import com.alibaba.fastjson.JSONObject;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

/**
 * @author zhangrb
 * docker连接、操作工具类
 * @date 2021/8/30
 */
public class DockerClientUtil {

//    public DockerClient connectDocker() {
//        /**
//         * 连接docker服务器(安全认证方式)
//         * @return DockerClient
//         * @param  dockerHost：docker服务器ip地址和端口号
//         *         dockercertPath：windows的密钥文件存放地址
//         *         dockerconfig：同Path，配置地址
//         *         apiVersion：dockerAPI的版本，通过docker version命令在docker服务器上获取版本号
//         */
//        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerTlsVerify(true)
//                .withDockerCertPath("D:/docker-java/").withDockerHost("tcp://192.168.184.128:2375")
//                .withDockerConfig("D:/docker-java/").withApiVersion("1.40").withRegistryUrl("https://index.docker.io/v1/")
//                .withRegistryUsername("dockeruser").withRegistryPassword("ilovedocker")
//                .withRegistryEmail("dockeruser@github.com").build();
//        DockerCmdExecFactory dockerCmdExecFactory =  new JerseyDockerCmdExecFactory()
//                .withReadTimeout(1000)
//                .withConnectTimeout(1000)
//                .withMaxTotalConnections(100)
//                .withMaxPerRouteConnections(10);
//        // 连接
//        DockerClient dockerClient = DockerClientBuilder.getInstance(config).withDockerCmdExecFactory(dockerCmdExecFactory).build();
//        Info info = dockerClient.infoCmd().exec();
//        System.out.println("docker的环境信息如下：=================");
//        System.out.println(info.toString());
//        return dockerClient;
//    }

    /**
     * 连接docker服务器
     * @return
     */
    public DockerClient connectDocker(String url){
//        DockerCmdExecFactory dockerCmdExecFactory =  new JerseyDockerCmdExecFactory()
//                .withReadTimeout(1000)
//                .withConnectTimeout(1000)
//                .withMaxTotalConnections(100)
//                .withMaxPerRouteConnections(10);
        // 连接
        DockerClient dockerClient = DockerClientBuilder.getInstance(url).build();
        Info info = dockerClient.infoCmd().exec();
        System.out.println("docker的环境信息如下：=================");
        System.out.println(info.toString());
        return dockerClient;
    }


    /**
     * 创建容器
     * @param client,imageName
     * @return
     */
    public CreateContainerResponse createContainers(DockerClient client, String imageName, Integer port, String name,String password){
        // 暴露的端口
        ExposedPort localPort = ExposedPort.tcp(5432);
        Ports portBindings = new Ports();
        // 绑定主机随机端口 -> docker服务器5432端口
//        portBindings.bind(localPort, Ports.Binding.empty());
        //映射端口8080—>80
        portBindings.bind(localPort, Ports.Binding.bindPort(port));
        CreateContainerResponse container = client.createContainerCmd(imageName)
//                .withName(name)
                .withPortBindings(portBindings)
                .withExposedPorts(localPort)
                .withEnv("GS_PASSWORD="+password)
                .exec();
        return container;
    }


    /**
     * 启动容器
     * @param client,containerId
     * @param containerId
     */
    public void startContainer(DockerClient client,String containerId){
        client.startContainerCmd(containerId).exec();
    }

    /**
     * 关闭容器
     * @param client,containerId
     * @param containerId
     */
    public void stopContainer(DockerClient client,String containerId){
        client.stopContainerCmd(containerId).exec();
    }

    /**
     * 删除容器
     * @param client,containerId
     * @param containerId
     */
    public void removeContainer(DockerClient client,String containerId){
        client.removeContainerCmd(containerId).exec();
    }

    /**
     * 删除镜像
     * @param client,imageId
     * @return
     */
    public void removeImage(DockerClient client,String imageId){
        client.removeImageCmd(imageId).exec();
    }

}
