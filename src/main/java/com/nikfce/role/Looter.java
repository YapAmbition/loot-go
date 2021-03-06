package com.nikfce.role;

import com.nikfce.action.*;
import com.nikfce.action.skill.AS_NormalAttack;
import com.nikfce.annotation.SkillSummary;
import com.nikfce.stage.RoundContext;
import com.nikfce.stage.RoundLifecycle;
import com.nikfce.thread.ThreadLocalMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 角色的基类
 * @author shenzhencheng 2022/3/1
 */
public class Looter implements Combative {

    private final Random random = new Random();

    public String name ;

    public String code ;
    // 基础属性
    private Properties basicProperties = new Properties(false);
    // 技能影响的属性值
    private Properties effectProperties = new Properties(false);
    // 临时属性
    private Properties tmpProperties = new Properties(false);

    private List<Skill> skillList = new ArrayList<>();

    public Looter() {}

    public Looter(String name, String code) {
        this(name, code, null);
    }

    public Looter(String name, String code, Properties properties) {
        this(name, code, properties, null);
    }

    public Looter(String name, String code, Properties properties, List<Skill> skillList) {
        this.name = name;
        this.code = code;
        this.basicProperties.mergeProperties(properties);
        Properties allLooterBasicProperties = Properties.PropertiesBuilder.create().setMaxHp(200).setHp(200).build();
        this.basicProperties.mergeProperties(allLooterBasicProperties);
        initSkill();
        if (skillList != null) {
            for (Skill skill : skillList) {
                addSkill(skill);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Properties getBasicProperties() {
        return basicProperties;
    }

    public void setBasicProperties(Properties basicProperties) {
        this.basicProperties = basicProperties;
    }

    public Properties getEffectProperties() {
        return effectProperties;
    }

    public Properties getTmpProperties() {
        return tmpProperties;
    }

    public void setEffectProperties(Properties effectProperties) {
        this.effectProperties = effectProperties;
    }

    public void setTmpProperties(Properties tmpProperties) {
        this.tmpProperties = tmpProperties;
    }

    public void setSkillList(List<Skill> skillList) {
        this.skillList = skillList;
    }

    /**
     * 添加默认技能
     */
    protected void initSkill() {
        addSkill(new AS_NormalAttack());
    }

    /**
     * 添加一个技能,添加时通过技能码做去重
     */
    public void addSkill(Skill skill) {
        SkillSummary curSkillSummary = skill.getClass().getAnnotation(SkillSummary.class);
        String curCode = curSkillSummary.code();
        for (Skill sk : skillList) {
            SkillSummary skillSummary = sk.getClass().getAnnotation(SkillSummary.class);
            String code = skillSummary.code();
            if (code.equalsIgnoreCase(curCode)) {
                return ;
            }
        }
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

    public List<Skill> getSkillList() {
        return skillList;
    }

    /**
     * 基础防御力
     */
    public double basicDefence() {
        return basicProperties.defence;
    }

    /**
     * 基础力量
     */
    public double basicStrength() {
        return basicProperties.strength;
    }

    /**
     * 基础体质
     */
    public double basicPhysique() {
        return basicProperties.getPhysique();
    }

    /**
     * 基础敏捷
     */
    public double basicAgility() {
        return basicProperties.agility;
    }

    /**
     * 基础属性简介
     */
    public String currentPropertiesSummary() {
        Properties summary = new Properties(false);
        summary.mergeProperties(basicProperties);
        summary.mergeProperties(effectProperties);
        summary.mergeProperties(tmpProperties);
        return summary.calChangeValueLog();
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
            skill.battleStart(this);
        }
    }

    @Override
    public void roundStart(RoundContext roundContext) {
        tmpProperties.clear();
        for (Skill skill : skillList) {
            skill.roundStart(this);
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

        ThreadLocalMap.getRecorder().record_f("[%s]决定对[%s]使用[%s]", name, targets.stream().map(Looter::getName).collect(Collectors.joining(",")), activeSKill.name());

        SkillContext actualSkillContext = new SkillContext(RoundLifecycle.BEFORE_ATTACK, this, targets, null);
        if (activeSKill instanceof IntensifyActiveSkill) {
            Effect effect = activeSKill.handle(actualSkillContext);
            intensified(this, activeSKill.name(), effect);
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
        ThreadLocalMap.getRecorder().record_f("[%s]受到了来自[%s]的攻击", name, attacker.getName());

        SkillContext skillContext = new SkillContext(RoundLifecycle.BEFORE_BE_ATTACK, this, Collections.singletonList(attacker), effect);
        passiveSkillAffect(skillContext);

        // 计算角色的闪避值,并进行闪避判定
        double dodge = currentDodge();
        if (random.nextInt(100) < dodge) {
            ThreadLocalMap.getRecorder().record_f("[%s]闪过了[%s]攻击,自己竟毫发未损!", name, attacker.getName());
        } else {
            // 计算并应用伤害,返回实际造成的伤害
            Effect actualEffect = calAndApplyEffect(skillContext);

            SkillContext actualSkillContext = new SkillContext(RoundLifecycle.AFTER_BE_ATTACK, this, Collections.singletonList(attacker), actualEffect);
            passiveSkillAffect(actualSkillContext);

            if (actualEffect.strike) {
                ThreadLocalMap.getRecorder().record_f("[%s]的这次攻击竟然产生了暴击!", attacker.getName());
            }
            ThreadLocalMap.getRecorder().record_f("[%s]最终受到[%s]点伤害", name, actualEffect.properties.hp);

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
     * 返回一份基础属性的复制
     */
    public Properties copyBasicProperties() {
        Properties properties = new Properties(false);
        properties.mergeProperties(basicProperties);
        return properties;
    }

    /**
     * 返回一份技能影响属性的复制
     */
    public Properties copyEffectProperties() {
        Properties properties = new Properties(false);
        properties.mergeProperties(effectProperties);
        return properties;
    }

    /**
     * 返回一份临时属性的复制
     */
    public Properties copyTmpProperties() {
        Properties properties = new Properties(false);
        properties.mergeProperties(tmpProperties);
        return properties;
    }

    /**
     * 强化/弱化技能生效
     * 注意,血量是会超出上限的,如果超过了上限,需要进行扣除,扣除逻辑很简单,把basic,effect,tmp的properties的hp值设置为maxHp即可
     * @param from 谁强化了你
     * @param byName 来自什么技能/物品
     * @param effect 强化的内容
     */
    @Override
    public void intensified(Looter from, String byName, Effect effect) {
        ThreadLocalMap.getRecorder().record_f("来自[%s]的[%s]生效了!", from.getName(), byName);
        ThreadLocalMap.getRecorder().record_f(effect.properties.calChangeValueLog());
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
            skill.roundEnd(this);
        }
    }

    @Override
    public void battleEnd() {
        effectProperties.clear();
        tmpProperties.clear();
        for (Skill skill : skillList) {
            skill.battleEnd(this);
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
