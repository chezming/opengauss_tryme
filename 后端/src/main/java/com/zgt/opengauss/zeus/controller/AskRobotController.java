package com.zgt.opengauss.zeus.controller;

import com.zgt.opengauss.zeus.annotation.PassToken;
import com.zgt.opengauss.zeus.entity.Result;
import com.zgt.opengauss.zeus.service.AskRobotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zzl
 * @date 2021/7/6
 */
@RequestMapping("/robot")
@RestController
@Api(tags = "问答机器人接口")
public class AskRobotController {

    @Autowired
    private AskRobotService askRobotService;

    @ApiOperation(value = "进行问答", notes = "传入问题进行问答")
    @GetMapping("/chat")
    @PassToken
    public Result<Object> chat(@RequestParam("gitId") String gitId, @RequestParam("question") String question){
        try {
            // TODO: 最好是能获得当前登录用户的id
            String answer = askRobotService.chat(gitId, question);
            return Result.OK(answer);
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

}
