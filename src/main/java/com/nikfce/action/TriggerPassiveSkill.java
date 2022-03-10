package com.nikfce.action;

/**
 * 触发被动技能
 * 这种类型的被动技能是在某些时间点可以触发生效的
 * 它可以增加使用者的属性,也可以降低目标的属性,甚至能调用彼此的方法
 * @author shenzhencheng 2022/3/9
 */
public interface TriggerPassiveSkill extends PassiveSkill {

    /**
     * 触发被动技能
     */
    void trigger(SkillContext skillContext);

}
