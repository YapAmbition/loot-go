package com.nikfce.role.hero;

import com.nikfce.action.skill.AS_SplitMyself;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

/**
 * @author shenzhencheng 2022/3/10
 */
public class YaQiang extends Looter {

    public YaQiang() {
        super("压强");
        initProperties();
    }

    private void initProperties() {
        Properties properties = Properties.PropertiesBuilder.create()
                .setPhysique(24)
                .setStrength(18)
                .setAgility(6)
                .setHp(20)
                .setMaxHp(20)
                .setAttack(6)
                .setDodge(15)
                .setApplyAttribute(true)
                .build();
        this.basicProperties.mergeProperties(properties);
    }

    @Override
    protected void initSkill() {
        super.initSkill();
        addSkill(new AS_SplitMyself());
    }
}
