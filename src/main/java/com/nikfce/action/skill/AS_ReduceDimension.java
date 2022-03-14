package com.nikfce.action.skill;

import com.nikfce.action.ActiveSKill;
import com.nikfce.action.Effect;
import com.nikfce.action.SkillContext;
import com.nikfce.annotation.SkillCode;
import com.nikfce.annotation.SkillName;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;
import com.nikfce.thread.ThreadLocalMap;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 降维打击,杨胖的专属技能,对[1]个敌人降低其30%基础防御并造成攻击力的1.2倍伤害
 * @author shenzhencheng 2022/3/10
 */
@SkillCode("SK_2")
@SkillName("降维打击")
public class AS_ReduceDimension implements ActiveSKill {

    private String skillName;
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
        double downDefence = target.basicDefence() * 0.3;
        boolean strike = me.calCauseStrike();
        ThreadLocalMap.getRecorder().record_f("%s对%s使出一招降维打击,减少其%s的防御并造成%s的伤害", me.name, target.name, downDefence, damage);
        if (strike) {
            damage = damage * 2.0;
        }
        Properties properties = Properties.PropertiesBuilder.create().setHp(-damage).setDefence(-downDefence).build();
        return new Effect(properties, strike);
    }

    @Override
    public boolean canUse(SkillContext skillContext) {
        return true;
    }

    @Override
    public String name() {
        if (skillName == null) {
            SkillName skillName = AS_NormalAttack.class.getAnnotation(SkillName.class);
            this.skillName = skillName.value();
        }
        return this.skillName;
    }

    @Override
    public void roundEnd(Looter myself) {
        target = null;
    }
}
