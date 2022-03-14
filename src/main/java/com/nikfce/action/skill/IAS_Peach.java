package com.nikfce.action.skill;

import com.nikfce.action.Effect;
import com.nikfce.action.IntensifyActiveSkill;
import com.nikfce.action.SkillContext;
import com.nikfce.annotation.SkillCode;
import com.nikfce.annotation.SkillName;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;
import com.nikfce.thread.ThreadLocalMap;

import java.util.Collections;
import java.util.List;

/**
 * 桃
 * 陶子的专属技能,当生命值小于50%的时候才能释放
 * 一场战斗中只能使用3次,可以回复自身50%的血量
 * @author shenzhencheng 2022/3/10
 */
@SkillCode("SK_5")
@SkillName("桃")
public class IAS_Peach implements IntensifyActiveSkill {

    private String skillName;
    private int canUseCount = 3;

    @Override
    public List<Looter> selectTargets(SkillContext skillContext) {
        return Collections.singletonList(skillContext.user);
    }

    @Override
    public Effect handle(SkillContext skillContext) {
        canUseCount --;
        Looter me = skillContext.user;
        double recover = me.currentMaxHp() / 2.0;
        ThreadLocalMap.getRecorder().record_f("%s不慌不忙地使用了%s,为自己恢复了%s的血量", me.name, name(), recover);
        Properties properties = Properties.PropertiesBuilder.create().setHp(recover).build();
        return new Effect(properties);
    }

    @Override
    public boolean canUse(SkillContext skillContext) {
        Looter me = skillContext.user;
        return me.currentHp() <= (me.currentMaxHp() / 2) && canUseCount > 0;
    }

    @Override
    public String name() {
        if (skillName == null) {
            SkillName skillName = AS_NormalAttack.class.getAnnotation(SkillName.class);
            this.skillName = skillName.value();
        }
        return this.skillName;
    }

    @Override
    public void battleStart(Looter myself) {
        canUseCount = 3;
    }
}
