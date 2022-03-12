package com.nikfce.register;

import com.alibaba.fastjson.JSON;
import com.nikfce.action.Skill;
import com.nikfce.annotation.LooterCode;
import com.nikfce.config.LootConfig;
import com.nikfce.role.*;
import com.nikfce.util.StringUtil;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 角色注册中心
 * @author shenzhencheng 2022/3/11
 */
public class LooterRegisterCenter {

    private static final Logger LOG = LoggerFactory.getLogger(LooterRegisterCenter.class);

    /**
     * 这里的value暂且同时Class和LooterDefinition,后续只存LooterDefinition
     */
    private static final Map<String, Object> LOOTER_MAP = new ConcurrentHashMap<>();

    public synchronized static void register(Class<? extends Looter> clazz) {
        LooterCode looterCode = clazz.getAnnotation(LooterCode.class);
        if (looterCode == null) {
            throw new RuntimeException("该角色没有LooterCode注解,请添加!");
        }
        String code = looterCode.value();
        if (code == null) {
            throw new RuntimeException("LooterCode不允许为null");
        }
        if (LOOTER_MAP.containsKey(code)) {
            throw new RuntimeException("该角色已经存在,不允许多次注册: " + code);
        }
        LOOTER_MAP.put(code, clazz);
    }

    /**
     * 通过LooterDefinition注册角色类
     */
    public synchronized static void register(LooterDefinition looterDefinition) {
        String looterCode = looterDefinition.getCode();
        if (StringUtil.isEmpty(looterCode)) {
            throw new RuntimeException("必须定义looterCode!");
        }
        if (LOOTER_MAP.containsKey(looterCode)) {
            System.out.println("该角色已经注册过,直接跳过: " + JSON.toJSONString(looterDefinition));
            return ;
        }

        String name = looterDefinition.getName();
        Properties properties = looterDefinition.getBasicProperties();

        if (StringUtil.isEmpty(name)) {
            throw new RuntimeException("必须定义name!");
        }

        if (properties == null || properties.isEmpty()) {
            throw new RuntimeException("必须定义基本属性且属性不能为空!");
        }

        if (looterDefinition.getSkillCodeList() == null) {
            looterDefinition.setSkillCodeList(new ArrayList<>());
        }

        LOOTER_MAP.put(looterCode, looterDefinition);
    }

    /**
     * 根据传入的角色码返回一个对应的角色实例
     */
    public static Looter generateLooter(String looterCode) {
        if (!LOOTER_MAP.containsKey(looterCode)) {
            throw new RuntimeException("该角色并没有注册:" + looterCode);
        }
        Object object = LOOTER_MAP.get(looterCode);
        if (object instanceof Class) {
            Class<? extends Looter> clazz = (Class<? extends Looter>) object;
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("生成Looter失败!" + looterCode);
            }
        } else if (object instanceof LooterDefinition) {
            LooterDefinition looterDefinition = (LooterDefinition) object;
            List<String> skillCodeList = looterDefinition.getSkillCodeList();
            List<Skill> skillList = new ArrayList<>();
            for (String skillCode : skillCodeList) {
                skillList.add(SkillRegisterCenter.generateSkill(skillCode));
            }
            return new DynamicLooter(looterDefinition.getName(), looterDefinition.getBasicProperties(), skillList);
        } else {
            throw new RuntimeException("未知的注册类型,looterCode: " + looterCode + ", class:" + object.getClass().getName());
        }
    }

    /**
     * 从源代码中注册角色
     */
    public static void registerLooterFromSrc() {
        String scanPackage = LootConfig.getInstance().getLooterPackage();
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> skillSet = reflections.getTypesAnnotatedWith(LooterCode.class);
        for (Class<?> clazz : skillSet) {
            if (clazz.isInterface()) {
                continue;
            }
            LooterRegisterCenter.register((Class<? extends Looter>)clazz);
        }
    }

    /**
     * 从配置文件中注册Looter
     */
    public static void registerLooterFromConfig() {
        try {
            List<LooterDefinition> definitionList = LooterConfigParser.parseAll();
            for (LooterDefinition looterDefinition : definitionList) {
                LOG.info("准备注册looter: {}, code: {}", looterDefinition.getName(), looterDefinition.getCode());
                LooterRegisterCenter.register(looterDefinition);
            }
        } catch (IOException e) {
            LOG.error("解析文件错误!", e);
            throw new RuntimeException("解析文件错误!", e);
        }

    }

}
