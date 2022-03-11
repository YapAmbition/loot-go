package com.nikfce.role.hero;

import com.nikfce.action.skill.AS_ReduceDimension;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

/**
 * 杨胖
 * 中等偏上的攻击和仅次于谢力的体质,他就是一个标准的二次元
 * @author shenzhencheng 2022/3/10
 */
public class YangPang extends Looter {

    public YangPang() {
        super("杨胖");
        initProperties();
    }

    private void initProperties() {
        Properties properties = Properties.PropertiesBuilder.create()
                .setPhysique(30)
                .setStrength(18)
                .setAgility(5)
                .setMaxHp(50)
                .setHp(50)
                .setAttack(5)
                .setApplyAttribute(true)
                .build();
        this.basicProperties.mergeProperties(properties);
    }

    @Override
    protected void initSkill() {
        super.initSkill();
        addSkill(new AS_ReduceDimension());
    }
}
