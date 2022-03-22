package com.nikfce.register;

import com.alibaba.fastjson.JSON;
import com.nikfce.action.Skill;
import com.nikfce.role.*;
import com.nikfce.role.Properties;
import com.nikfce.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
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
    private static final Map<String, LooterDefinition> LOOTER_MAP = new ConcurrentHashMap<>();

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
        LOG.info("成功注册looter: {}, code: {}", name, looterCode);
    }

    /**
     * 根据传入的角色码返回一个对应的角色实例
     */
    public static Looter generateLooter(String looterCode) {
        if (!LOOTER_MAP.containsKey(looterCode)) {
            throw new RuntimeException("该角色并没有注册:" + looterCode);
        }
        LooterDefinition looterDefinition = LOOTER_MAP.get(looterCode);
        List<String> skillCodeList = looterDefinition.getSkillCodeList();
        List<Skill> skillList = new ArrayList<>();
        for (String skillCode : skillCodeList) {
            skillList.add(SkillRegisterCenter.generateSkill(skillCode));
        }
        return new DynamicLooter(looterDefinition.getName(), looterDefinition.getCode(), looterDefinition.getBasicProperties(), skillList);
    }

    /**
     * 从配置文件中注册Looter
     */
    public static void registerLooterFromConfig() {
        try {
            List<LooterDefinition> definitionList = LooterConfigParser.parseAll();
            for (LooterDefinition looterDefinition : definitionList) {
                LooterRegisterCenter.register(looterDefinition);
            }
        } catch (IOException e) {
            LOG.error("解析文件错误!", e);
            throw new RuntimeException("解析文件错误!", e);
        }
    }

    /**
     * 列出所有的looter码
     */
    public static Set<String> listLooters() {
        return new HashSet<>(LOOTER_MAP.keySet());
    }

    /**
     * 列出所有用户可以选的looter码
     */
    public static Set<String> listCanChoiceLooters() {
        Set<String> result = new HashSet<>();
        for (String code : LOOTER_MAP.keySet()) {
            if (LOOTER_MAP.get(code).isCanChoice()) {
                result.add(code);
            }
        }
        return result;
    }

}
