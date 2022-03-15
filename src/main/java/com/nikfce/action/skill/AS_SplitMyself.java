package com.nikfce.action.skill;

import com.nikfce.action.ActiveSKill;
import com.nikfce.action.Effect;
import com.nikfce.action.SkillContext;
import com.nikfce.annotation.SkillSummary;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;
import com.nikfce.thread.ThreadLocalMap;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 压强专属技能,分则能成
 * 对[1]个目标额外造成2次50%攻击的伤害
 * @author shenzhencheng 2022/3/10
 */
@SkillSummary(code = "SK_3", name = "分则能成", desc = "对1个目标额外造成2次50%攻击的伤害")
public class AS_SplitMyself implements ActiveSKill {

    private final String skillCode;
    private final String skillName;
    private final String skillDesc;
    private Looter target;

    public AS_SplitMyself() {
        SkillSummary skillSummary = AS_SplitMyself.class.getAnnotation(SkillSummary.class);
        this.skillCode = skillSummary.code();
        this.skillName = skillSummary.name();
        this.skillDesc = skillSummary.desc();
    }

    @Override
    public List<Looter> selectTargets(SkillContext skillContext) {
        List<Looter> enemies = skillContext.enemy;
        List<Looter> candidate = enemies.stream().filter(a -> !a.isDead()).collect(Collectors.toList());
        target = candidate.get(new Random().nextInt(candidate.size()));
        return Collections.singletonList(target);
    }

    @Override
    public Effect handle(SkillContext skillContext) {
        Looter me = skillContext.user;
        double damage = me.currentAttack();
        double extraDamage = damage * 0.5;
        ThreadLocalMap.getRecorder().record_f("%s使出一招%s,制造出两个自己的分身先对%s进行攻击,然后本体再对其进行攻击!", me.getName(), name(), target.getName());
        for (int i = 0 ; i < 2 ; i ++) {
            ThreadLocalMap.getRecorder().record_f("%s正在进行第%s段伤害为%s的攻击", me.getName(), (i+1), extraDamage);
            boolean strike = me.calCauseStrike();
            Properties properties = Properties.PropertiesBuilder.create().setHp(-(strike ? 2.0 * extraDamage : extraDamage)).build();
            Effect extraEffect = new Effect(properties, strike);
            target.beAttack(me, extraEffect);
        }
        ThreadLocalMap.getRecorder().record_f("%s本体对%s进行伤害为%s的总攻击!", me.getName(), target.getName(), damage);
        boolean strike = me.calCauseStrike();
        Properties properties = Properties.PropertiesBuilder.create().setHp(-(strike ? 2.0 * damage : damage)).build();
        return new Effect(properties, strike);
    }

    @Override
    public boolean canUse(SkillContext skillContext) {
        return true;
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
    public void roundEnd(Looter myself) {
        target = null;
    }
}
