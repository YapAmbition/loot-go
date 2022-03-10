package com.nikfce.action;

/**
 * @author shenzhencheng 2022/3/1
 */
public interface Skill {

    /**
     * 该技能当前是否能用
     */
    boolean canUse(SkillContext skillContext);

    /**
     * 技能名
     */
    String name();

}
