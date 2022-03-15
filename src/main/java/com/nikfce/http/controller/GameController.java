package com.nikfce.http.controller;

import com.nikfce.http.dto.LooterDTO;
import com.nikfce.http.dto.NextDto;
import com.nikfce.http.handler.GameHandler;
import com.nikfce.http.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenzhencheng 2022/3/14
 */
@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameHandler gameHandler;

    /**
     * 传入名字,注册用户,返回token
     */
    @RequestMapping("/register")
    public String register(@RequestBody GameRegisterRequest request) {
        return gameHandler.register(request.getName());
    }

    @RequestMapping("/next")
    public NextDto next() {
        return gameHandler.next();
    }

    @RequestMapping("/choiceLooter")
    public boolean choiceLooter(@RequestBody ChoiceLooterRequest request) {
        if (request.getLooterCode() == null) {
            throw new RuntimeException("请选择Looter!");
        }
        gameHandler.choiceLooter(request.getLooterCode());
        return true;
    }

    @RequestMapping("/choiceScene")
    public boolean choiceScene(@RequestBody ChoiceSceneRequest request) {
        if (request.getSceneName() == null) {
            throw new RuntimeException("请选择Scene!");
        }
        gameHandler.choiceScene(request.getSceneName());
        return true;
    }

    /**
     * 退出Scene
     */
    @RequestMapping("/exitScene")
    public boolean exitScene() {
        gameHandler.exitScene();
        return true;
    }

    @RequestMapping("/choiceFlow")
    public FlowResponse choiceFlow(@RequestBody ChoiceFlowRequest request) {
        if (request.getFlowName() == null) {
            throw new RuntimeException("请选择Flow!");
        }
        return gameHandler.choiceFlow(request.getFlowName());
    }

    @RequestMapping("/showMyLooter")
    public LooterDTO showMyLooter() {
        return gameHandler.showMyLooter();
    }

    /**
     * 重开游戏,把原来的缓存删掉
     */
    @RequestMapping("/resetGame")
    public boolean resetGame() {
        return gameHandler.resetGame();
    }

}
