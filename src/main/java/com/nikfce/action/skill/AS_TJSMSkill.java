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
import java.util.stream.Collectors;

/**
 * 田忌赛马,李坤专属技能,技能会选取[1]个攻击力最高的敌人为目标,造成1.5倍伤害
 * @author shenzhencheng 2022/3/10
 */
@SkillSummary(code = "SK_4", name = "田忌赛马", desc = "选取1个攻击力最高的敌人为目标,造成1.5倍伤害")
public class AS_TJSMSkill implements ActiveSKill {

    private final String skillCode;
    private final String skillName;
    private final String skillDesc;

    public AS_TJSMSkill() {
        SkillSummary skillSummary = AS_TJSMSkill.class.getAnnotation(SkillSummary.class);
        this.skillCode = skillSummary.code();
        this.skillName = skillSummary.name();
        this.skillDesc = skillSummary.desc();
    }

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
        Looter me = skillContext.user;
        double damage = me.currentAttack() * 1.5;
        boolean strike = me.calCauseStrike();
        ThreadLocalMap.getRecorder().record_f("%s使出一招%s,选取敌方的上等马进行攻击", me.getName(), name());
        Properties properties = Properties.PropertiesBuilder.create()
                .setHp(-(strike ? 2.0 * damage : damage))
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
}
