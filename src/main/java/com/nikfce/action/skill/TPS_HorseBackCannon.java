package com.nikfce.action.skill;

import com.nikfce.action.Effect;
import com.nikfce.action.SkillContext;
import com.nikfce.action.TriggerPassiveSkill;
import com.nikfce.annotation.SkillSummary;
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
 * 会在自己受到伤害之后,给对手造成相当于自己攻击力0.7倍的伤害
 * @author shenzhencheng 2022/3/10
 */
@SkillSummary(code = "SK_9", name = "马后炮", desc = "在被攻击之后,有50%的概率触发,给对手造成相当于自己攻击力0.7倍的伤害")
public class TPS_HorseBackCannon implements TriggerPassiveSkill {

    private final String skillCode;
    private final String skillName;
    private final String skillDesc;
    private final Random random = new Random();

    public TPS_HorseBackCannon() {
        SkillSummary skillSummary = TPS_HorseBackCannon.class.getAnnotation(SkillSummary.class);
        this.skillCode = skillSummary.code();
        this.skillName = skillSummary.name();
        this.skillDesc = skillSummary.desc();
    }

    @Override
    public boolean canUse(SkillContext skillContext) {
        return skillContext.roundLifecycle == RoundLifecycle.AFTER_BE_ATTACK && random.nextInt(10) < 5;
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
    public void trigger(SkillContext skillContext) {
        List<Looter> looterList = skillContext.enemy;
        Looter me = skillContext.user;
        double damage = me.currentAttack() * 0.7;
        ThreadLocalMap.getRecorder().record_f("[%s]的被动技能[%s]生效,将对攻击他的敌人[%s]造成伤害为[%s]的反击", me.getName(), name(), looterList.stream().map(Looter::getName).collect(Collectors.joining(",")), damage);
        for (Looter looter : looterList) {
            boolean strike = me.calCauseStrike();
            Properties properties = Properties.PropertiesBuilder.create().setHp(-(strike ? 2.0 * damage : damage)).build();
            Effect effect = new Effect(properties, strike);
            looter.beAttack(me, effect);
        }
    }
}
