package com.nikfce.action.skill;

import com.nikfce.action.Effect;
import com.nikfce.action.PropertiesPassiveSkill;
import com.nikfce.annotation.SkillSummary;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;
import com.nikfce.thread.ThreadLocalMap;

/**
 * 身体棒棒
 * 增加20%基础体质
 * @author shenzhencheng 2022/3/11
 */
@SkillSummary(code = "SK_6", name = "身体棒棒", desc = "增加20%基础体质")
public class PPS_PerfectBody implements PropertiesPassiveSkill {

    private final String skillCode;
    private final String skillName;
    private final String skillDesc;

    public PPS_PerfectBody() {
        SkillSummary skillSummary = PPS_PerfectBody.class.getAnnotation(SkillSummary.class);
        this.skillCode = skillSummary.code();
        this.skillName = skillSummary.name();
        this.skillDesc = skillSummary.desc();
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
    public void battleStart(Looter myself) {
        double physique = myself.basicPhysique();
        double increment = physique * 0.2;
        ThreadLocalMap.getRecorder().record_f("%s发动[%s],增加自己%s的体质", myself.getName(), name(), increment);
        Properties properties = Properties.PropertiesBuilder.create().setPhysique(increment).setApplyAttribute(true).build();
        myself.intensified(myself, skillName, new Effect(properties));
    }
}
