package com.nikfce.action.skill;

import com.nikfce.action.Effect;
import com.nikfce.action.PropertiesPassiveSkill;
import com.nikfce.annotation.SkillSummary;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;
import com.nikfce.thread.ThreadLocalMap;

/**
 * 肌肉拉满
 * 增加20%的基础力量值
 * @author shenzhencheng 2022/3/11
 */
@SkillSummary(code = "SK_8", name = "肌肉拉满", desc = "增加20%的基础力量值")
public class PPS_PerfectMuscle implements PropertiesPassiveSkill {

    private final String skillCode;
    private final String skillName;
    private final String skillDesc;

    public PPS_PerfectMuscle() {
        SkillSummary skillSummary = PPS_PerfectMuscle.class.getAnnotation(SkillSummary.class);
        this.skillCode = skillSummary.code();
        this.skillName = skillSummary.name();
        this.skillDesc = skillSummary.desc();
    }

    @Override
    public void battleStart(Looter myself) {
        double strength = myself.basicStrength();
        double increment = strength * 0.20;
        ThreadLocalMap.getRecorder().record_f("[%s]的被动技能[%s],为自己增加了[%s]的基础力量", myself.getName(), name(), increment);
        Properties properties = Properties.PropertiesBuilder.create().setStrength(increment).setApplyAttribute(true).build();
        myself.intensified(myself, skillName, new Effect(properties));
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
