package com.nikfce.game;

import com.nikfce.config.LootConfig;
import com.nikfce.register.LooterRegisterCenter;
import com.nikfce.register.SkillRegisterCenter;
import com.nikfce.role.Looter;
import com.nikfce.stage.Battle;
import java.io.IOException;
import java.util.*;

/**
 * 普通的一局对战游戏
 * @author shenzhencheng 2022/3/11
 */
public class NormalGame {

    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        LootConfig.init();
        // 注册代码中定义的技能
        SkillRegisterCenter.registerSkillFromConfig();
        // 注册代码中定义的角色
        LooterRegisterCenter.registerLooterFromSrc();
        // 注册配置文件中定义的角色
        LooterRegisterCenter.registerLooterFromConfig();

        Looter looter1 = LooterRegisterCenter.generateLooter("LOOTER_101");
        Looter looter2 = LooterRegisterCenter.generateLooter("LOOTER_1");

        new Battle(Collections.singletonList(looter1), Collections.singletonList(looter2)).battleStart();

    }

}
