package com.nikfce.action;

import com.nikfce.role.Looter;

import java.util.List;

/**
 * 主动技能,它能返回要攻击的目标,且能真正进行攻击,返回Effect
 * @author shenzhencheng 2022/3/9
 */
public interface ActiveSKill extends Skill {

    List<Looter> selectTargets(SkillContext skillContext);

    Effect handle(SkillContext skillContext);

}
