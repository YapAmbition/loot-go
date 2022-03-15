package com.nikfce.action.skill;

import com.nikfce.action.ActiveSKill;
import com.nikfce.action.Effect;
import com.nikfce.action.SkillContext;
import com.nikfce.annotation.SkillSummary;
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
@SkillSummary(code = "SK_2", name = "降维打击", desc = "对1个敌人降低其30%基础防御并造成攻击力的1.2倍伤害")
public class AS_ReduceDimension implements ActiveSKill {

    private final String skillCode;
    private final String skillName;
    private final String skillDesc;
    private Looter target;

    public AS_ReduceDimension() {
        SkillSummary skillSummary = AS_ReduceDimension.class.getAnnotation(SkillSummary.class);
        this.skillCode = skillSummary.code();
        this.skillName = skillSummary.name();
        this.skillDesc = skillSummary.desc();
    }

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
        ThreadLocalMap.getRecorder().record_f("%s对%s使出一招降维打击,减少其%s的防御并造成%s的伤害", me.getName(), target.getName(), downDefence, damage);
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
    public String code() {
        return skillCode;
    }

    @Override
    public String name() {
        return skillName;
    }

    @Override
    public String desc() {
        return skillDesc;
    }

    @Override
    public void roundEnd(Looter myself) {
        target = null;
    }
}
