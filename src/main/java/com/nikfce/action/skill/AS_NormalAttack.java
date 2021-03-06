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
 * 普通攻击,对随机[1]个敌人造成攻击力100%的伤害
 * @author shenzhencheng 2022/3/10
 */
@SkillSummary(code = "SK_1", name = "普通攻击", desc = "对随机1个敌人造成攻击力100%的伤害")
public class AS_NormalAttack implements ActiveSKill {

    private final String skillCode;
    private final String skillName;
    private final String skillDesc;

    private Looter target;

    public AS_NormalAttack() {
        SkillSummary skillSummary = AS_NormalAttack.class.getAnnotation(SkillSummary.class);
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
        double damage = me.currentAttack();
        boolean strike = me.calCauseStrike();
        ThreadLocalMap.getRecorder().record_f("[%s]对[%s]使出一招普通攻击,这一招一看就有足足[%s]的威力", me.getName(), target.getName(), damage);
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
