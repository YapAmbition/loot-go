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

    private final Random random = new Random();

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
        Properties allLooterBasicProperties = Properties.PropertiesBuilder.create().setMaxHp(200).setHp(200).build();
        this.basicProperties.mergeProperties(allLooterBasicProperties);
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

    public double currentDodge() {
        return basicProperties.dodge + effectProperties.dodge + tmpProperties.dodge;
    }

    public double currentStrike() {
        return basicProperties.strike + effectProperties.strike + tmpProperties.strike;
    }

    public double currentLuck() {
        return basicProperties.luck + effectProperties.luck + tmpProperties.luck;
    }

    public double currentAttack() {
        return basicProperties.attack + effectProperties.attack + tmpProperties.attack;
    }

    /**
     * 基础防御力
     */
    public double basicDefence() {
        return basicProperties.defence;
    }

    /**
     * 计算是否产生暴击
     */
    public boolean calCauseStrike() {
        double strike = currentStrike();
        return random.nextInt(100) < strike;
    }

    @Override
    public void battleStart() {
        effectProperties.clear();
        tmpProperties.clear();
        for (Skill skill : skillList) {
            skill.battleStart();
        }
    }

    @Override
    public void roundStart(RoundContext roundContext) {
        tmpProperties.clear();
        for (Skill skill : skillList) {
            skill.roundStart();
        }
    }

    /**
     * 先选择一个技能
     * 让技能去选择待攻击的目标
     * 计算技能能造成的伤害(Effect)
     * 如果是强化型技能
     *      自身的被动技能不生效,直接调用自己的intensified(),表示受到了强化
     * 如果不是强化型技能
     *      触发被动技能生效
     *      调用敌人的beAttack(),表示敌人被攻击了
     * 回合结束
     */
    @Override
    public void attack(RoundContext roundContext) {
        SkillContext choiceSkillContext = new SkillContext(RoundLifecycle.BEFORE_ATTACK, this, roundContext.targets, null);
        ActiveSKill activeSKill = choiceActiveSkill(choiceSkillContext);

        SkillContext preSkillContext = new SkillContext(RoundLifecycle.BEFORE_ATTACK, this, roundContext.targets, null);
        List<Looter> targets = activeSKill.selectTargets(preSkillContext);

        System.out.println(name + "决定对" + targets.stream().map(i -> i.name).collect(Collectors.joining(",")) + "使用" + activeSKill.name());

        SkillContext actualSkillContext = new SkillContext(RoundLifecycle.BEFORE_ATTACK, this, targets, null);
        if (activeSKill instanceof IntensifyActiveSkill) {
            Effect effect = activeSKill.handle(actualSkillContext);
            intensified(this, effect);
        } else {
            passiveSkillAffect(actualSkillContext);
            Effect effect = activeSKill.handle(actualSkillContext);
            for (Looter target : actualSkillContext.enemy) {
                target.beAttack(this, effect);
            }
        }

        roundEnd(roundContext);
    }



    @Override
    public void beAttack(Looter attacker, Effect effect) {
        System.out.println(name + "受到了来自" + attacker.name + "的攻击");

        SkillContext skillContext = new SkillContext(RoundLifecycle.BEFORE_BE_ATTACK, this, Collections.singletonList(attacker), effect);
        passiveSkillAffect(skillContext);

        // 计算角色的闪避值,并进行闪避判定
        double dodge = currentDodge();
        if (random.nextInt(100) < dodge) {
            System.out.printf("%s闪过了%s攻击,自己竟毫发未损!%n", name, attacker.name);
        } else {
            // 计算并应用伤害,返回实际造成的伤害
            Effect actualEffect = calAndApplyEffect(skillContext);

            SkillContext actualSkillContext = new SkillContext(RoundLifecycle.AFTER_BE_ATTACK, this, Collections.singletonList(attacker), actualEffect);
            passiveSkillAffect(actualSkillContext);

            if (actualEffect.strike) {
                System.out.printf("%s的这次攻击竟然产生了暴击!%n", attacker.name);
            }
            System.out.println(name + "最终受到" + actualEffect.properties.hp + "点伤害");

            // 回调攻击者的攻击结束接口,并告诉它实际造成的伤害是多少
            attacker.attackFinish(this, actualEffect);
        }
    }

    @Override
    public void attackFinish(Looter target, Effect actualEffect) {
        SkillContext skillContext = new SkillContext(RoundLifecycle.AFTER_ATTACK, this, Collections.singletonList(target), actualEffect);
        passiveSkillAffect(skillContext);
    }

    /**
     * 注意,血量是会超出上限的,如果超过了上限,需要进行扣除,扣除逻辑很简单,把basic,effect,tmp的properties的hp值设置为maxHp即可
     * @param from 谁强化了你
     * @param effect 强化的内容
     */
    @Override
    public void intensified(Looter from, Effect effect) {
        System.out.printf("来自%s的强化技能生效!%n", from.name);
        System.out.println(effect.properties.calChangeValueLog());
        effectProperties.mergeProperties(effect.properties);
        if (currentHp() > currentMaxHp()) {
            basicProperties.hp = basicProperties.maxHp;
            effectProperties.hp = effectProperties.maxHp;
            tmpProperties.hp = tmpProperties.maxHp;
        }
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
        for (Skill skill : skillList) {
            skill.roundEnd();
        }
    }

    @Override
    public void battleEnd() {
        effectProperties.clear();
        tmpProperties.clear();
        for (Skill skill : skillList) {
            skill.battleEnd();
        }
    }

    /**
     * 选择一个主动技能
     */
    private ActiveSKill choiceActiveSkill(SkillContext skillContext) {
        List<ActiveSKill> candidate = new ArrayList<>();
        for (Skill skill : skillList) {
            if (skill instanceof ActiveSKill && skill.canUse(skillContext)) {
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
        return new Effect(actualProperties, comeInEffect.strike);
    }

}
