package com.nikfce.action.skill;

import com.nikfce.action.Effect;
import com.nikfce.action.SkillContext;
import com.nikfce.action.TriggerPassiveSkill;
import com.nikfce.annotation.SkillCode;
import com.nikfce.annotation.SkillName;
import com.nikfce.role.Looter;
import com.nikfce.role.Properties;
import com.nikfce.stage.RoundLifecycle;
import com.nikfce.thread.ThreadLocalMap;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 马后炮,谢力的专属技能,
 * 只有在被攻击之后,有50%的概率触发
 * 会在自己受到伤害之后,给对手造成相当于自己力量值2倍的伤害
 * @author shenzhencheng 2022/3/10
 */
@SkillCode("SK_9")
@SkillName("马后炮")
public class TPS_HorseBackCannon implements TriggerPassiveSkill {

    private String skillName;
    private final Random random = new Random();

    @Override
    public boolean canUse(SkillContext skillContext) {
        return skillContext.roundLifecycle == RoundLifecycle.AFTER_BE_ATTACK && random.nextInt(10) < 5;
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
    public void trigger(SkillContext skillContext) {
        List<Looter> looterList = skillContext.enemy;
        Looter me = skillContext.user;
        double damage = me.currentStrength() * 2.0;
        ThreadLocalMap.getRecorder().record_f("%s的被动技能[%s]生效,将对攻击他的敌人:%s进行伤害为:%s的反击", me.name, name(), looterList.stream().map(a -> a.name).collect(Collectors.joining(",")), damage);
        for (Looter looter : looterList) {
            boolean strike = me.calCauseStrike();
            Properties properties = Properties.PropertiesBuilder.create().setHp(-(strike ? 2.0 * damage : damage)).build();
            Effect effect = new Effect(properties, strike);
            looter.beAttack(me, effect);
        }
    }
}
