package com.nikfce.role;

/**
 * @author shenzhencheng 2022/3/1
 */
public class Properties {

    // attribute
    public double physique = 0;
    public double strength = 0;
    public double agility = 0;

    // properties
    public double maxHp = 0.0;
    public double hp = 0.0;
    public double attack = 0.0;
    public double defence = 0.0;
    public double strike = 0.0; // 暴击率
    public double speed = 0.0;
    public double luck = 0.0;

    /**
     * 是否应用attribute对战斗数值造成影响
     * 一般如果只设置attribute就想让整个属性生效就传true,如果明确不想让属性影响战斗数值就传false
     */
    public Properties(boolean applyAttribute) {
        if (applyAttribute) {
            applyAttribute();
        }
    }

    /**
     * 应用属性
     */
    private void applyAttribute() {
        applyPhysique(physique);
        applyStrength(strength);
        applyAgility(agility);
    }

    /**
     * 1敏捷 = 3速度 + 1爆率
     */
    private void applyAgility(double agility) {
        this.speed += agility * 3;
        this.strike += agility * 1;
    }

    /**
     * 1力量 = 3攻击
     */
    private void applyStrength(double strength) {
        this.attack += strength * 3;
    }

    /**
     * 1体质 = 1防御 + 5血量
     */
    private void applyPhysique(double physique) {
        this.defence += physique * 1;
        this.maxHp += physique * 5;
        this.hp += physique * 5;
    }

    /**
     * 敏捷属性增加
     */
    public void addAgility(double agility) {
        this.agility += agility;
        applyAgility(agility);
    }

    /**
     * 力量属性增加
     */
    public void addStrength(double strength) {
        this.strength += strength;
        applyStrength(strength);
    }

    /**
     * 体质属性增加
     */
    public void addPhysique(double physique) {
        this.physique += physique;
        applyPhysique(physique);
    }

    /**
     * 单纯的属性merge,不做属性应用
     */
    public void mergeProperties(Properties properties) {
        if (properties != null) {
            this.physique += properties.physique;
            this.strength += properties.strength;
            this.agility += properties.agility;
            this.maxHp += properties.maxHp;
            this.hp += properties.hp;
            this.attack += properties.attack;
            this.defence += properties.defence;
            this.strike += properties.strike;
            this.speed += properties.speed;
            this.luck += properties.luck;
        }
    }

    public void clear() {
        physique = 0;
        strength = 0;
        agility = 0;
        maxHp = 0.0;
        hp = 0.0;
        attack = 0.0;
        defence = 0.0;
        strike = 0.0;
        speed = 0.0;
        luck = 0.0;
    }

    public static class PropertiesBuilder {
        private final Properties template;
        private boolean applyAttribute = false;
        private PropertiesBuilder() {
            template = new Properties(false);
        }
        public static PropertiesBuilder create() {
            return new PropertiesBuilder();
        }
        public PropertiesBuilder setPhysique(double physique) {
            template.physique = physique;
            return this;
        }
        public PropertiesBuilder setStrength(double strength) {
            template.strength = strength;
            return this;
        }
        public PropertiesBuilder setAgility(double agility) {
            template.agility = agility;
            return this;
        }
        public PropertiesBuilder setMaxHp(double maxHp) {
            template.maxHp = maxHp;
            return this;
        }
        public PropertiesBuilder setHp(double hp) {
            template.hp = hp;
            return this;
        }
        public PropertiesBuilder setAttack(double attack) {
            template.attack = attack;
            return this;
        }
        public PropertiesBuilder setDefence(double defence) {
            template.defence = defence;
            return this;
        }
        public PropertiesBuilder setStrike(double strike) {
            template.strike = strike;
            return this;
        }
        public PropertiesBuilder setSpeed(double speed) {
            template.speed = speed;
            return this;
        }
        public PropertiesBuilder setLuck(double luck) {
            template.luck = luck;
            return this;
        }
        public PropertiesBuilder setApplyAttribute(boolean applyAttribute) {
            this.applyAttribute = applyAttribute;
            return this;
        }
        public Properties build() {
            Properties properties = new Properties(false);
            properties.mergeProperties(template);
            if (applyAttribute) {
                properties.applyAttribute();
            }
            return properties;
        }
    }

}
