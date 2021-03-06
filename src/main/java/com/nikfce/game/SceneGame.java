package com.nikfce.game;

import com.alibaba.fastjson.JSON;
import com.nikfce.config.LootConfig;
import com.nikfce.register.LooterRegisterCenter;
import com.nikfce.register.SceneRegisterCenter;
import com.nikfce.register.SkillRegisterCenter;
import com.nikfce.role.Looter;
import com.nikfce.scene.Flow;
import com.nikfce.scene.IntrudeContext;
import com.nikfce.scene.Scene;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * 进入场景的游戏
 * @author shenzhencheng 2022/3/13
 */
public class SceneGame {

    private static String waitInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    private static void waitSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void init() {
        // 首先同样的,要初始化配置,技能,角色,场景
        LootConfig.init();
        // 注册代码中定义的技能
        SkillRegisterCenter.registerSkillFromSrc();
        // 注册配置文件中定义的角色
        LooterRegisterCenter.registerLooterFromConfig();
        // 注册场景
        SceneRegisterCenter.registerSceneFromConfig();
    }

    public static void main(String[] args) {
        init();
        waitSecond();
        System.out.println("输入你要选择的英雄...");
        String looterCode = waitInput();
        Looter looter = LooterRegisterCenter.generateLooter(looterCode);
        waitSecond();
        System.out.println("角色生成完毕! -> " + looter.getName());
        System.out.println(JSON.toJSONString(SceneRegisterCenter.showSceneList()));
        System.out.println("请输入要进入的场景名...");
        String sceneName = waitInput();
        Scene scene = SceneRegisterCenter.generateScene(sceneName);
        waitSecond();
        System.out.println("场景生成完毕! -> " + scene.getName());
        IntrudeContext intrudeContext = new IntrudeContext(Collections.singletonList(looter));

        boolean win = true ;
        String desc = null;
        while (win && (desc = scene.clearScene(intrudeContext)) == null) {
            List<Flow> displayFlows = scene.nextFlows(intrudeContext);
            System.out.println("你的面前呈现出了几个Flow,请选择一个攻击:");
            System.out.println(displayFlows);
            String flowName = waitInput();
            win = scene.interFlow(intrudeContext, flowName);
        }

        if (win) {
            System.out.println("恭喜你战胜通关" + sceneName + "!" + desc);
        } else {
            System.out.println("游戏失败!");
        }
    }

}
