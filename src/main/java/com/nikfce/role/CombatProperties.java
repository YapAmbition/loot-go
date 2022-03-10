package com.nikfce.role;

/**
 * 战斗属性
 * @author shenzhencheng 2022/3/9
 */
public class CombatProperties {

    public double maxHp = 0;
    public double hp = 0;
    public double attack = 0.0;
    public double defence = 0.0;
    public double strike = 0.0;
    public double speed = 0.0;
    public double luck = 0;

    public CombatProperties(Attribute base) {
        applyPhysique(base.physique);
        applyStrength(base.strength);
        applyAgility(base.agility);
    }

    /**
     * 1敏捷 = 3速度 + 1爆率
     */
    private void applyAgility(double agility) {
        this.speed += agility * 3;
        this.strike += agility * 1;
    }

    /**
     * 1力量 = 1防御 + 3攻击
     */
    private void applyStrength(double strength) {
        this.defence += strength * 1;
        this.attack += strength * 3;
    }

    /**
     * 1体质 = 2防御 + 5血量
     */
    private void applyPhysique(double physique) {
        this.defence += physique * 2;
        this.maxHp += physique * 5;
        this.hp += physique * 5;
    }

}
