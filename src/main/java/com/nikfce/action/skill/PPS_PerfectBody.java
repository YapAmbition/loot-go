package com.nikfce.action.skill;

import com.nikfce.action.Effect;
import com.nikfce.action.PropertiesPassiveSkill;
import com.nikfce.annotation.SkillCode;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;
import com.nikfce.thread.ThreadLocalMap;

/**
 * 身体棒棒
 * 增加20%基础体质
 * @author shenzhencheng 2022/3/11
 */
@SkillCode("SK_6")
public class PPS_PerfectBody implements PropertiesPassiveSkill {

    @Override
    public String name() {
        return "身体棒棒";
    }

    @Override
    public void battleStart(Looter myself) {
        double physique = myself.basicPhysique();
        double increment = physique * 0.2;
        ThreadLocalMap.getRecorder().record_f("%s发动[%s],增加自己%s的体质", myself.name, name(), increment);
        Properties properties = Properties.PropertiesBuilder.create().setPhysique(increment).setApplyAttribute(true).build();
        myself.intensified(myself, new Effect(properties));
    }
}
