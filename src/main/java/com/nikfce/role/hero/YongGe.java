package com.nikfce.role.hero;

import com.nikfce.action.skill.PPS_PerfectBody;
import com.nikfce.action.skill.PPS_PerfectLeg;
import com.nikfce.action.skill.PPS_PerfectMuscle;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

/**
 * 勇哥
 * 属性一般,没有主动技能,天生自带三个提高属性的被动,虽然初始属性一般但是叫他属性怪也不为过
 * @author shenzhencheng 2022/3/11
 */
public class YongGe extends Looter {

    public YongGe() {
        super("勇哥");
        initProperties();
    }

    private void initProperties() {
        Properties properties = Properties.PropertiesBuilder.create()
                .setPhysique(25)
                .setStrength(18)
                .setAgility(7)
                .setApplyAttribute(true)
                .build();
        this.basicProperties.mergeProperties(properties);
    }

    @Override
    protected void initSkill() {
        super.initSkill();
        addSkill(new PPS_PerfectBody());
        addSkill(new PPS_PerfectMuscle());
        addSkill(new PPS_PerfectLeg());
    }
}
