package com.nikfce.action.skill;

import com.nikfce.action.ActiveSKill;
import com.nikfce.action.Effect;
import com.nikfce.action.SkillContext;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 田忌赛马,李坤专属技能,技能会选取攻击力最高的敌人为目标,造成1.5倍伤害
 * @author shenzhencheng 2022/3/10
 */
public class AS_TJSMSkill implements ActiveSKill {
    @Override
    public List<Looter> selectTargets(SkillContext skillContext) {
        List<Looter> enemies = skillContext.enemy;
        List<Looter> candidate = enemies.stream().filter(a -> !a.isDead()).collect(Collectors.toList());
        Looter target = candidate.get(0);
        double attack = target.currentAttack();
        for (int i = 1 ; i < candidate.size() ; i ++) {
            if (candidate.get(i).currentAttack() > attack) {
                attack = candidate.get(i).currentAttack();
                target = candidate.get(i);
            }
        }
        return Collections.singletonList(target);
    }

    @Override
    public Effect handle(SkillContext skillContext) {
        // 造成1.5倍伤害
        Looter me = skillContext.user;
        double attack = me.currentAttack() * 1.5;
        Properties properties = Properties.PropertiesBuilder.create()
                .setHp(-attack)
                .build();
        return new Effect(properties);
    }

    @Override
    public boolean canUse(SkillContext skillContext) {
        return true;
    }

    @Override
    public String name() {
        return "田忌赛马";
    }
}
