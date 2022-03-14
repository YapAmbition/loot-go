package com.nikfce.action.skill;

import com.nikfce.action.Effect;
import com.nikfce.action.PropertiesPassiveSkill;
import com.nikfce.annotation.SkillCode;
import com.nikfce.annotation.SkillName;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;
import com.nikfce.thread.ThreadLocalMap;

/**
 * 完美大腿
 * 增加20%的基础敏捷值
 * @author shenzhencheng 2022/3/11
 */
@SkillCode("SK_7")
@SkillName("完美大腿")
public class PPS_PerfectLeg implements PropertiesPassiveSkill {

    private String skillName;

    @Override
    public void battleStart(Looter myself) {
        double agility = myself.basicAgility();
        double increment = agility * 0.20;
        ThreadLocalMap.getRecorder().record_f("%s发动[%s],增加自己%s的敏捷", myself.name, name(), increment);
        Properties properties = Properties.PropertiesBuilder.create().setAgility(increment).setApplyAttribute(true).build();
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
