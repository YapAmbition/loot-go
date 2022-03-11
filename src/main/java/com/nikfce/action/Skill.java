package com.nikfce.action;

import com.nikfce.role.Looter;

/**
 * @author shenzhencheng 2022/3/1
 */
public interface Skill {

    /**
     * 战斗开始时初始化工作
     */
    default void battleStart(Looter myself) {}

    /**
     * 回合开始时初始化工作
     */
    default void roundStart(Looter myself) {}

    /**
     * 该技能当前是否能用
     */
    boolean canUse(SkillContext skillContext);

    /**
     * 技能名
     */
    String name();

    /**
     * 回合结束时收尾工作
     */
    default void roundEnd(Looter myself) {}

    /**
     * 战斗结束时的收尾工作
     */
    default void battleEnd(Looter myself) {}

}
