package com.nikfce.action.skill;

import com.nikfce.action.ActiveSKill;
import com.nikfce.action.Effect;
import com.nikfce.action.SkillContext;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 普通攻击,对随机[1]个敌人造成攻击力100%的伤害
 * @author shenzhencheng 2022/3/10
 */
public class AS_NormalAttack implements ActiveSKill {
    @Override
    public List<Looter> selectTargets(SkillContext skillContext) {
        List<Looter> enemies = skillContext.enemy;
        List<Looter> candidate = enemies.stream().filter(a -> !a.isDead()).collect(Collectors.toList());
        Looter target = candidate.get(new Random().nextInt(candidate.size()));
        return Collections.singletonList(target);
    }

    @Override
    public Effect handle(SkillContext skillContext) {
        Looter me = skillContext.user;
        double damage = me.currentAttack();
        boolean strike = me.calCauseStrike();
        System.out.printf("%s对使出一招普通攻击,这一招一看就有足足%s的威力", me.name, damage);
        if (strike) {
            damage = damage * 2.0;
        }
        Properties properties = Properties.PropertiesBuilder.create()
                .setHp(-damage)
                .build();
        return new Effect(properties, strike);
    }

    @Override
    public boolean canUse(SkillContext skillContext) {
        return true;
    }

    @Override
    public String name() {
        return "普通攻击";
    }
}
