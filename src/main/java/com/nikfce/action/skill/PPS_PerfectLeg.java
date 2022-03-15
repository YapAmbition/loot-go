package com.nikfce.action.skill;

import com.nikfce.action.Effect;
import com.nikfce.action.PropertiesPassiveSkill;
import com.nikfce.annotation.SkillSummary;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;
import com.nikfce.thread.ThreadLocalMap;

/**
 * 完美大腿
 * 增加20%的基础敏捷值
 * @author shenzhencheng 2022/3/11
 */
@SkillSummary(code = "SK_7", name = "完美大腿", desc = "增加20%的基础敏捷值")
public class PPS_PerfectLeg implements PropertiesPassiveSkill {

    private final String skillCode;
    private final String skillName;
    private final String skillDesc;

    public PPS_PerfectLeg() {
        SkillSummary skillSummary = PPS_PerfectLeg.class.getAnnotation(SkillSummary.class);
        this.skillCode = skillSummary.code();
        this.skillName = skillSummary.name();
        this.skillDesc = skillSummary.desc();
    }

    @Override
    public void battleStart(Looter myself) {
        double agility = myself.basicAgility();
        double increment = agility * 0.20;
        ThreadLocalMap.getRecorder().record_f("[%s]发动[%s]被动技能,增加自己[%s]的敏捷", myself.getName(), name(), increment);
        Properties properties = Properties.PropertiesBuilder.create().setAgility(increment).setApplyAttribute(true).build();
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
