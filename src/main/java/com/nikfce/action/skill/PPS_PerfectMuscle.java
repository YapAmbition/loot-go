package com.nikfce.action.skill;

import com.nikfce.action.Effect;
import com.nikfce.action.PropertiesPassiveSkill;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

/**
 * 肌肉拉满
 * 增加20%的基础力量值
 * @author shenzhencheng 2022/3/11
 */
public class PPS_PerfectMuscle implements PropertiesPassiveSkill {

    @Override
    public void battleStart(Looter myself) {
        double strength = myself.basicStrength();
        double increment = strength * 0.20;
        System.out.printf("%s的被动技能[%s],为自己增加了%s的基础力量%n", myself.name, name(), increment);
        Properties properties = Properties.PropertiesBuilder.create().setStrength(increment).setApplyAttribute(true).build();
        myself.intensified(myself, new Effect(properties));
    }

    @Override
    public String name() {
        return "肌肉拉满";
    }
}