package com.nikfce.action.skill;

import com.nikfce.action.Effect;
import com.nikfce.action.IntensifyActiveSkill;
import com.nikfce.action.SkillContext;
import com.nikfce.annotation.SkillSummary;
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
@SkillSummary(code = "SK_5", name = "桃", desc = "当生命值小于50%的时候才能释放,恢复50%的血量(一场战斗中只能使用2次)")
public class IAS_Peach implements IntensifyActiveSkill {

    private static final int COUNT_LIMIT = 2;
    private final String skillCode;
    private final String skillName;
    private final String skillDesc;
    private int canUseCount = COUNT_LIMIT;

    public IAS_Peach() {
        SkillSummary skillSummary = IAS_Peach.class.getAnnotation(SkillSummary.class);
        this.skillCode = skillSummary.code();
        this.skillName = skillSummary.name();
        this.skillDesc = skillSummary.desc();
    }

    @Override
    public List<Looter> selectTargets(SkillContext skillContext) {
        return Collections.singletonList(skillContext.user);
    }

    @Override
    public Effect handle(SkillContext skillContext) {
        canUseCount --;
        Looter me = skillContext.user;
        double recover = me.currentMaxHp() / 2.0;
        ThreadLocalMap.getRecorder().record_f("%s不慌不忙地使用了%s,为自己恢复了%s的血量", me.getName(), name(), recover);
        Properties properties = Properties.PropertiesBuilder.create().setHp(recover).build();
        return new Effect(properties);
    }

    @Override
    public boolean canUse(SkillContext skillContext) {
        Looter me = skillContext.user;
        return me.currentHp() <= (me.currentMaxHp() / 2) && canUseCount > 0;
    }

    @Override
    public String code() {
        return skillCode;
    }

    @Override
    public String name() {
        return skillName;
    }

    @Override
    public String desc() {
        return skillDesc;
    }

    @Override
    public void battleStart(Looter myself) {
        canUseCount = COUNT_LIMIT;
    }
}
