package com.nikfce.register;

import com.nikfce.action.Skill;
import com.nikfce.annotation.SkillCode;
import com.nikfce.annotation.SkillName;
import com.nikfce.config.LootConfig;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 技能注册中心,根据技能码去重,重复的技能码不能注册并直接报错
 * @author shenzhencheng 2022/3/11
 */
public class SkillRegisterCenter {

    private static final Logger LOG = LoggerFactory.getLogger(SkillRegisterCenter.class);
    private static final Map<String, Class<? extends Skill>> SKILL_MAP = new ConcurrentHashMap<>();

    public synchronized static void register(Class<? extends Skill> clazz) {
        SkillCode skillCode = clazz.getAnnotation(SkillCode.class);
        if (skillCode == null) {
            throw new RuntimeException("该技能没有SkillCode注解,请添加!");
        }
        String code = skillCode.value();
        if (code == null) {
            throw new RuntimeException("SkillCode不允许为null");
        }

        SkillName skillName = clazz.getAnnotation(SkillName.class);
        String name = skillName.value();
        if (name == null) {
            throw new RuntimeException("SkillName不允许为null");
        }

        // 已经注册过的直接跳过
        if (SKILL_MAP.containsKey(code)) {
            return ;
        }

        SKILL_MAP.put(code, clazz);
        LOG.info("成功注册技能: code: {}, name: {}", code, name);
    }

    /**
     * 根据传入的技能码返回一个对应的技能实例
     */
    public static Skill generateSkill(String skillCode) {
        if (!SKILL_MAP.containsKey(skillCode)) {
            throw new RuntimeException("该技能并没有注册:" + skillCode);
        }
        Class<? extends Skill> clazz = SKILL_MAP.get(skillCode);
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("生成技能失败!" + skillCode, e);
        }
    }

    /**
     * 注册源文件中的技能
     */
    public static void registerSkillFromSrc() {
        String scanPackage = LootConfig.getInstance().getSkillPackage();
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> skillSet = reflections.getTypesAnnotatedWith(SkillCode.class);
        for (Class<?> clazz : skillSet) {
            if (clazz.isInterface()) {
                continue;
            }
            SkillRegisterCenter.register((Class<? extends Skill>)clazz);
        }
    }

    public static void main(String[] args) {
        LootConfig.init();
        registerSkillFromSrc();
    }

}
