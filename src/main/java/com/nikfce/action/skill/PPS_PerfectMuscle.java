package com.nikfce.action.skill;

import com.nikfce.action.Effect;
import com.nikfce.action.PropertiesPassiveSkill;
import com.nikfce.annotation.SkillCode;
import com.nikfce.annotation.SkillName;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;
import com.nikfce.thread.ThreadLocalMap;

/**
 * 肌肉拉满
 * 增加20%的基础力量值
 * @author shenzhencheng 2022/3/11
 */
@SkillCode("SK_8")
@SkillName("肌肉拉满")
public class PPS_PerfectMuscle implements PropertiesPassiveSkill {

    private String skillName;

    @Override
    public void battleStart(Looter myself) {
        double strength = myself.basicStrength();
        double increment = strength * 0.20;
        ThreadLocalMap.getRecorder().record_f("%s的被动技能[%s],为自己增加了%s的基础力量", myself.name, name(), increment);
        Properties properties = Properties.PropertiesBuilder.create().setStrength(increment).setApplyAttribute(true).build();
        myself.intensified(myself, new Effect(properties));
    }

    @Override
    public String name() {
        if (skillName == null) {
            SkillName skillName = AS_NormalAttack.class.getAnnotation(SkillName.class);
            this.skillName = skillName.value();
        }
        return this.skillName;
    }
}
