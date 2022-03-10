package com.nikfce.role.hero;

import com.nikfce.action.skill.AS_TJSMSkill;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

/**
 * 李坤,以敏捷高著称,虽然身板和攻击力一般,但是速度极快,一般人可能没见到他就已经不省人事了
 * @author shenzhencheng 2022/3/10
 */
public class LiKun extends Looter {

    public LiKun() {
        super("李坤");
        initProperties();
    }

    private void initProperties() {
        Properties properties = Properties.PropertiesBuilder.create()
                .setPhysique(25)
                .setStrength(18)
                .setAgility(10)
                .setApplyAttribute(true)
                .build();
        this.basicProperties.mergeProperties(properties);
    }

    @Override
    protected void initSkill() {
        super.initSkill();
        addSkill(new AS_TJSMSkill());
    }
}
