package com.zgt.opengauss.zeus;

import com.zgt.opengauss.zeus.service.AskRobotService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AskRobotTest {

    @Autowired
    AskRobotService askRobotService;

    @Test
    public void testGetToken() throws Exception{
        String token = askRobotService.getToken();
        System.out.println(token);
    }

    @Test
    public void testChat() throws Exception{
        String token = askRobotService.chat(null, "这是什么？");
        System.out.println(token);
    }
}
