package com.nikfce.register;

import com.alibaba.fastjson.JSON;
import com.nikfce.action.Skill;
import com.nikfce.annotation.LooterCode;
import com.nikfce.role.DynamicLooter;
import com.nikfce.role.Looter;
import com.nikfce.role.LooterDefinition;
import com.nikfce.role.Properties;
import com.nikfce.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 角色注册中心
 * @author shenzhencheng 2022/3/11
 */
public class LooterRegisterCenter {

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
    public static Looter generateLooter(String looterCode) throws InstantiationException, IllegalAccessException {
        if (!LOOTER_MAP.containsKey(looterCode)) {
            throw new RuntimeException("该角色并没有注册:" + looterCode);
        }
        Object object = LOOTER_MAP.get(looterCode);
        if (object instanceof Class) {
            Class<? extends Looter> clazz = (Class<? extends Looter>) object;
            return clazz.newInstance();
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

}
