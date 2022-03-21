package com.nikfce.http.controller;

import com.nikfce.annotation.UserPermission;
import com.nikfce.config.LootConfig;
import com.nikfce.http.dto.CommonResponse;
import com.nikfce.http.dto.LooterDTO;
import com.nikfce.http.dto.NextDto;
import com.nikfce.http.handler.GameHandler;
import com.nikfce.http.request.*;
import com.nikfce.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shenzhencheng 2022/3/14
 */
@RestController
@RequestMapping("/game")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class GameController {

    @Autowired
    private GameHandler gameHandler;

    /**
     * 传入名字,注册用户,返回token
     */
    @RequestMapping("/register")
    public CommonResponse<String> register(@RequestBody GameRegisterRequest request, HttpServletResponse httpServletResponse) {
        try {
            String token = gameHandler.register(request.getName());
            // 给前端塞cookie
            Cookie cookie = new Cookie(LootConfig.getInstance().getTokenCookieKey(), token);
            httpServletResponse.addCookie(cookie);
            return new CommonResponse.CommonResponseBuilder<String>().setCode(200).setType("register").setData(token).build();
        } catch (Throwable t) {
            return new CommonResponse.CommonResponseBuilder<String>()
                                                .setCode(500)
                                                .setType("error")
                                                .setErrMsg(t.getMessage())
                                                .build();
        }
    }

    @RequestMapping("/next")
    @UserPermission
    public CommonResponse<Object> next() {
        try {
            NextDto nextDto = gameHandler.next();
            return new CommonResponse.CommonResponseBuilder<>().setCode(200).setType("next").setData(nextDto).build();
        } catch (Throwable t) {
            return new CommonResponse.CommonResponseBuilder<>()
                    .setCode(500)
                    .setType("error")
                    .setErrMsg(t.getMessage())
                    .build();
        }
    }

    @RequestMapping("/choiceLooter")
    @UserPermission
    public CommonResponse<String> choiceLooter(@RequestBody ChoiceLooterRequest request) {
        try {
            if (StringUtil.isEmpty(request.getLooterCode())) {
                throw new RuntimeException("请选择Looter!");
            }
            gameHandler.choiceLooter(request.getLooterCode());
            return new CommonResponse.CommonResponseBuilder<String>().setCode(200).setType("choiceLooter").setData(request.getLooterCode()).build();
        } catch (Throwable t) {
            return new CommonResponse.CommonResponseBuilder<String>()
                    .setCode(500)
                    .setType("error")
                    .setErrMsg(t.getMessage())
                    .build();
        }
    }

    @RequestMapping("/choiceScene")
    @UserPermission
    public CommonResponse<String> choiceScene(@RequestBody ChoiceSceneRequest request) {
        try {
            if (StringUtil.isEmpty(request.getSceneName())) {
                throw new RuntimeException("请选择Scene!");
            }
            gameHandler.choiceScene(request.getSceneName());
            return new CommonResponse.CommonResponseBuilder<String>().setCode(200).setType("choiceScene").setData(request.getSceneName()).build();
        } catch (Throwable t) {
            return new CommonResponse.CommonResponseBuilder<String>()
                    .setCode(500)
                    .setType("error")
                    .setErrMsg(t.getMessage())
                    .build();
        }
    }

    /**
     * 退出Scene
     */
    @RequestMapping("/exitScene")
    @UserPermission
    public CommonResponse<Object> exitScene() {
        try {
            gameHandler.exitScene();
            return new CommonResponse.CommonResponseBuilder<>().setCode(200).setType("exitScene").setData(true).build();
        } catch (Throwable t) {
            return new CommonResponse.CommonResponseBuilder<>()
                    .setCode(500)
                    .setType("error")
                    .setErrMsg(t.getMessage())
                    .build();
        }
    }

    @RequestMapping("/choiceFlow")
    @UserPermission
    public CommonResponse<Object> choiceFlow(@RequestBody ChoiceFlowRequest request) {
        try {
            if (StringUtil.isEmpty(request.getFlowName())) {
                throw new RuntimeException("请选择Flow!");
            }
            FlowResponse flowResponse = gameHandler.choiceFlow(request.getFlowName());
            return new CommonResponse.CommonResponseBuilder<>().setCode(200).setType("choiceFlow").setData(flowResponse).build();
        } catch (Throwable t) {
            return new CommonResponse.CommonResponseBuilder<>()
                    .setCode(500)
                    .setType("error")
                    .setErrMsg(t.getMessage())
                    .build();
        }
    }

    @RequestMapping("/showMyLooter")
    @UserPermission
    public CommonResponse<Object> showMyLooter() {
        try {
            LooterDTO looterDTO = gameHandler.showMyLooter();
            return new CommonResponse.CommonResponseBuilder<>().setCode(200).setType("showMyLooter").setData(looterDTO).build();
        } catch (Throwable t) {
            return new CommonResponse.CommonResponseBuilder<>()
                    .setCode(500)
                    .setType("error")
                    .setErrMsg(t.getMessage())
                    .build();
        }
    }

    /**
     * 重开游戏,把原来的缓存删掉
     */
    @RequestMapping("/resetGame")
    @UserPermission
    public CommonResponse<Object> resetGame(HttpServletResponse response) {
        try {
            boolean b = gameHandler.resetGame();
            Cookie cookie = new Cookie(LootConfig.getInstance().getTokenCookieKey(), null);
            response.addCookie(cookie);
            return new CommonResponse.CommonResponseBuilder<>().setCode(200).setType("resetGame").setData(b).build();
        } catch (Throwable t) {
            return new CommonResponse.CommonResponseBuilder<>()
                    .setCode(500)
                    .setType("error")
                    .setErrMsg(t.getMessage())
                    .build();
        }
    }

}
