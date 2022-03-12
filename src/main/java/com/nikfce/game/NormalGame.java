package com.nikfce.game;

import com.alibaba.fastjson.JSON;
import com.nikfce.action.Skill;
import com.nikfce.annotation.LooterCode;
import com.nikfce.annotation.SkillCode;
import com.nikfce.register.LooterRegisterCenter;
import com.nikfce.register.SkillRegisterCenter;
import com.nikfce.role.Looter;
import com.nikfce.role.LooterConfigParser;
import com.nikfce.role.LooterDefinition;
import com.nikfce.stage.Battle;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.*;

/**
 * @author shenzhencheng 2022/3/11
 */
public class NormalGame {

    private static void registerSkill() {
        String scanPackage = "com.nikfce.action";
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> skillSet = reflections.getTypesAnnotatedWith(SkillCode.class);
        for (Class<?> clazz : skillSet) {
            if (clazz.isInterface()) {
                continue;
            }
            SkillRegisterCenter.register((Class<? extends Skill>)clazz);
        }
    }

    private static void registerLooterFromSrc() {
        String scanPackage = "com.nikfce.role";
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> skillSet = reflections.getTypesAnnotatedWith(LooterCode.class);
        for (Class<?> clazz : skillSet) {
            if (clazz.isInterface()) {
                continue;
            }
            LooterRegisterCenter.register((Class<? extends Looter>)clazz);
        }
    }

    private static void registerLooterFromConfig() throws IOException {
        List<LooterDefinition> definitionList = LooterConfigParser.parse();
        for (LooterDefinition looterDefinition : definitionList) {
            System.out.println("准备注册looter: " + JSON.toJSONString(looterDefinition));
            LooterRegisterCenter.register(looterDefinition);
        }
    }

    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        // 注册代码中定义的技能
        registerSkill();
        // 注册代码中定义的角色
        registerLooterFromSrc();
        // 注册配置文件中定义的角色
        registerLooterFromConfig();

        Looter looter1 = LooterRegisterCenter.generateLooter("LOOTER_101");
        Looter looter2 = LooterRegisterCenter.generateLooter("LOOTER_1");

        new Battle(Collections.singletonList(looter1), Collections.singletonList(looter2)).battleStart();

    }

}
