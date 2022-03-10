package com.nikfce.role.hero;

import com.nikfce.action.skill.IAS_Peach;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

/**
 * @author shenzhencheng 2022/3/10
 */
public class TaoZi extends Looter {

    public TaoZi() {
        super("陶子");
        initProperties();
    }

    private void initProperties() {
        Properties properties = Properties.PropertiesBuilder.create()
                .setPhysique(28)
                .setStrength(22)
                .setAgility(4)
                .setMaxHp(30)
                .setHp(30)
                .setApplyAttribute(true)
                .build();
        this.basicProperties.mergeProperties(properties);
    }

    @Override
    protected void initSkill() {
        super.initSkill();
        addSkill(new IAS_Peach());
    }
}
