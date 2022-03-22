package com.nikfce.role;

import java.util.List;

/**
 * 角色的定义类,用于动态创建角色类
 * @author shenzhencheng 2022/3/11
 */
public class LooterDefinition {

    /**
     * 角色类的名字
     */
    private String name;

    /**
     * 唯一标识码
     */
    private String code;

    /**
     * 用户是否可选
     * 1表示可选,0表示不可选
     */
    private boolean canChoice;

    /**
     * 初始属性
     */
    private Properties basicProperties;

    /**
     * 技能码
     */
    private List<String> skillCodeList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Properties getBasicProperties() {
        return basicProperties;
    }

    public void setBasicProperties(Properties basicProperties) {
        this.basicProperties = basicProperties;
    }

    public List<String> getSkillCodeList() {
        return skillCodeList;
    }

    public void setSkillCodeList(List<String> skillCodeList) {
        this.skillCodeList = skillCodeList;
    }

    public boolean isCanChoice() {
        return canChoice;
    }

    public void setCanChoice(boolean canChoice) {
        this.canChoice = canChoice;
    }
}
