package com.nikfce.action;

import com.nikfce.role.Looter;

/**
 * 属性被动技能,即只要获得了这个技能,就能增加自己的属性
 * @author shenzhencheng 2022/3/9
 */
public interface PropertiesPassiveSkill extends PassiveSkill {

    /**
     * 战斗开始时调用,为looter增加属性值
     */
    @Override
    void battleStart(Looter myself);

    @Override
    default boolean canUse(SkillContext skillContext) {
        return true;
    }
}
