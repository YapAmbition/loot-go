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
 * 降维打击,杨胖的专属技能,对[1]个敌人降低其20%基础防御并造成攻击力的1.2倍伤害
 * @author shenzhencheng 2022/3/10
 */
public class AS_ReduceDimension implements ActiveSKill {

    private Looter target;

    @Override
    public List<Looter> selectTargets(SkillContext skillContext) {
        List<Looter> enemies = skillContext.enemy;
        List<Looter> candidate = enemies.stream().filter(a -> !a.isDead()).collect(Collectors.toList());
        target = candidate.get(new Random().nextInt(candidate.size()));
        return Collections.singletonList(target);
    }

    @Override
    public Effect handle(SkillContext skillContext) {
        Looter me = skillContext.user;
        double damage = me.currentAttack() * 1.2;
        double downDefence = target.basicDefence() * 0.2;
        System.out.printf("%s对%s使出一招降维打击,减少其%s的防御并造成%s的伤害%n", me.name, target.name, downDefence, damage);
        Properties properties = Properties.PropertiesBuilder.create().setHp(-damage).setDefence(-downDefence).build();
        return new Effect(properties);
    }

    @Override
    public boolean canUse(SkillContext skillContext) {
        return true;
    }

    @Override
    public String name() {
        return "降维打击";
    }
}
