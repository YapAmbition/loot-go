package com.nikfce.role;

import com.nikfce.action.*;
import com.nikfce.action.skill.AS_NormalAttack;
import com.nikfce.stage.RoundContext;
import com.nikfce.stage.RoundLifecycle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author shenzhencheng 2022/3/1
 */
public class Looter implements Combative {

    public final String name ;
    // 基础属性
    protected final Properties basicProperties = new Properties(false);
    // 技能影响的属性值
    protected final Properties effectProperties = new Properties(false);
    // 临时属性
    protected final Properties tmpProperties = new Properties(false);

    protected final List<Skill> skillList = new ArrayList<>();

    public Looter(String name) {
        this(name, null);
    }

    public Looter(String name, Properties properties) {
        this.name = name;
        this.basicProperties.mergeProperties(properties);
        initSkill();
    }

    /**
     * 添加默认技能
     */
    protected void initSkill() {
        skillList.add(new AS_NormalAttack());
    }

    /**
     * 添加一个技能
     */
    public void addSkill(Skill skill) {
        skillList.add(skill);
    }

    public boolean isDead() {
        return currentHp() <= 0;
    }

    public double currentStrength() {
        return basicProperties.strength + effectProperties.strength + tmpProperties.strength;
    }

    public double currentSpeed() {
        return basicProperties.speed + effectProperties.speed + tmpProperties.speed;
    }

    public double currentHp() {
        return basicProperties.hp + effectProperties.hp + tmpProperties.hp;
    }

    public double currentMaxHp() {
        return basicProperties.maxHp + effectProperties.maxHp + tmpProperties.maxHp;
    }

    public double currentLuck() {
        return basicProperties.luck + effectProperties.luck + tmpProperties.luck;
    }

    public double currentAttack() {
        return basicProperties.attack + effectProperties.attack + tmpProperties.attack;
    }

    public double basicDefence() {
        return basicProperties.defence;
    }

    @Override
    public void roundStart(RoundContext roundContext) {
        tmpProperties.clear();
    }

    @Override
    public void attack(RoundContext roundContext) {
        ActiveSKill activeSKill = choiceActiveSkill();

        SkillContext preSkillContext = new SkillContext(RoundLifecycle.BEFORE_ATTACK, this, roundContext.targets, null);
        List<Looter> targets = activeSKill.selectTargets(preSkillContext);

        System.out.println(name + "决定对" + String.join(",", targets.stream().map(i -> i.name).collect(Collectors.toList())) + "使用" + activeSKill.name());

        SkillContext actualSkillContext = new SkillContext(RoundLifecycle.BEFORE_ATTACK, this, targets, null);
        passiveSkillAffect(actualSkillContext);

        Effect effect = activeSKill.handle(actualSkillContext);
        for (Looter target : actualSkillContext.enemy) {
            target.beAttack(this, effect);
        }

        roundEnd(roundContext);
    }



    @Override
    public void beAttack(Looter attacker, Effect effect) {
        System.out.println(name + "受到了来自" + attacker.name + "的攻击");

        SkillContext skillContext = new SkillContext(RoundLifecycle.BEFORE_BE_ATTACK, this, Collections.singletonList(attacker), effect);
        passiveSkillAffect(skillContext);

        // 计算并应用伤害,返回实际造成的伤害
        Effect actualEffect = calAndApplyEffect(skillContext);

        SkillContext actualSkillContext = new SkillContext(RoundLifecycle.AFTER_BE_ATTACK, this, Collections.singletonList(attacker), actualEffect);
        passiveSkillAffect(actualSkillContext);

        System.out.println(name + "最终受到" + actualEffect.properties.hp + "点伤害");

        // 回调攻击者的攻击结束接口,并告诉它实际造成的伤害是多少
        attacker.attackFinish(this, actualEffect);
    }

    @Override
    public void attackFinish(Looter target, Effect actualEffect) {
        SkillContext skillContext = new SkillContext(RoundLifecycle.AFTER_ATTACK, this, Collections.singletonList(target), actualEffect);
        passiveSkillAffect(skillContext);
    }

    /**
     * 被动技能生效
     */
    public void passiveSkillAffect(SkillContext skillContext) {
        for (Skill skill : skillList) {
            if (skill instanceof TriggerPassiveSkill) {
                if (skill.canUse(skillContext)) {
                    ((TriggerPassiveSkill) skill).trigger(skillContext);
                }
            }
        }
    }

    @Override
    public void roundEnd(RoundContext roundContext) {
        tmpProperties.clear();
    }

    /**
     * 选择一个主动技能
     */
    private ActiveSKill choiceActiveSkill() {
        List<ActiveSKill> candidate = new ArrayList<>();
        for (Skill skill : skillList) {
            if (skill instanceof ActiveSKill) {
                candidate.add((ActiveSKill)skill);
            }
        }
        if (candidate.isEmpty()) {
            throw new RuntimeException("找不到任何主动技能,战斗失败");
        }
        return candidate.get(new Random().nextInt(candidate.size()));
    }

    /**
     * 根据传入的影响,以及自身的属性计算并应用伤害(暂时不支持对生命上限造成影响)
     * 先计算属性变化,最后再计算血量
     * @return 实际的影响
     */
    private Effect calAndApplyEffect(SkillContext skillContext) {
        Effect comeInEffect = skillContext.actualEffect;
        // 计算属性
        effectProperties.addPhysique(comeInEffect.properties.physique);
        effectProperties.addStrength(comeInEffect.properties.strength);
        effectProperties.addAgility(comeInEffect.properties.agility);
        // 计算战斗数值
        effectProperties.attack += comeInEffect.properties.attack;
        effectProperties.defence += comeInEffect.properties.defence;
        effectProperties.strike += comeInEffect.properties.strike;
        effectProperties.speed += comeInEffect.properties.speed;
        effectProperties.luck += comeInEffect.properties.luck;
        // 计算实际伤害
        double damage = comeInEffect.properties.hp; // 这里的damage是个负数,表示减少目标血量
        double defence = Math.max(basicProperties.defence + effectProperties.defence, 0);
        damage += defence;
        damage = Math.min(damage, 0); // 如果伤害绝对值没有防御绝对值高的话,就造成0点伤害,而不是让目标加血
        // 应用伤害
        effectProperties.hp += damage;
        // 封装成Effect,并返回
        Properties actualProperties = new Properties(false);
        actualProperties.mergeProperties(effectProperties);
        actualProperties.hp = damage;
        return new Effect(actualProperties);
    }

}
