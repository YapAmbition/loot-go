package com.nikfce.role.hero;

import com.nikfce.action.skill.IAS_Peach;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

/**
 * 陶子:专属技能: `桃`，是目前唯一一个可以回复生命值的技能，3次的使用次数，在打持久战中足以耗死对手
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
