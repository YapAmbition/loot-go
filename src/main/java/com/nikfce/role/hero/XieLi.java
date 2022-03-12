package com.nikfce.role.hero;

import com.nikfce.action.skill.TPS_HorseBackCannon;
import com.nikfce.annotation.LooterCode;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

/**
 * 谢力,大肉排,优秀的体格和防御力,超高的攻击是他的杀手锏,虽然速度很慢,但他有天赋技能[马后炮],能在敌人攻击他的时候做出反击
 * 想杀他的人大部分都自己先死了
 * @author shenzhencheng 2022/3/10
 */
@LooterCode("LOOTER_3")
public class XieLi extends Looter {

    public XieLi() {
        super("谢力");
        initProperties();
    }

    private void initProperties() {
        Properties properties = Properties.PropertiesBuilder.create()
                .setPhysique(35)
                .setStrength(30)
                .setAgility(2)
                .setApplyAttribute(true)
                .build();
        this.basicProperties.mergeProperties(properties);
    }

    @Override
    protected void initSkill() {
        super.initSkill();
        addSkill(new TPS_HorseBackCannon());
    }
}
