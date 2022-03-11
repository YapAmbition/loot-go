package com.nikfce.action.skill;

import com.nikfce.action.Effect;
import com.nikfce.action.PropertiesPassiveSkill;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

/**
 * 完美大腿
 * 增加20%的基础敏捷值
 * @author shenzhencheng 2022/3/11
 */
public class PPS_PerfectLeg implements PropertiesPassiveSkill {

    @Override
    public void battleStart(Looter myself) {
        double agility = myself.basicAgility();
        double increment = agility * 0.20;
        System.out.printf("%s发动[%s],增加自己%s的敏捷%n", myself.name, name(), increment);
        Properties properties = Properties.PropertiesBuilder.create().setAgility(increment).setApplyAttribute(true).build();
        myself.intensified(myself, new Effect(properties));
    }

    @Override
    public String name() {
        return "完美大腿";
    }
}
