package com.nikfce.action.skill;

import com.nikfce.action.ActiveSKill;
import com.nikfce.action.Effect;
import com.nikfce.action.SkillContext;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 普通攻击,对随机[1]个敌人造成攻击力100%的伤害
 * @author shenzhencheng 2022/3/10
 */
public class NormalAttackSkill implements ActiveSKill {
    @Override
    public List<Looter> selectTargets(SkillContext skillContext) {
        List<Looter> enemies = skillContext.enemy;
        Looter target = enemies.get(new Random().nextInt(enemies.size()));
        return Collections.singletonList(target);
    }

    @Override
    public Effect handle(SkillContext skillContext) {
        Looter me = skillContext.user;
        double attack = me.currentAttack();
        Properties properties = Properties.PropertiesBuilder.create()
                .setHp(-attack)
                .build();
        return new Effect(properties);
    }

    @Override
    public boolean canUse(SkillContext skillContext) {
        return true;
    }
}
