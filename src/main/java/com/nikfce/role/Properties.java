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
    public double dodge = 0.0; // 闪避率
    public double luck = 0.0;

    public Properties() {
        this(false);
    }

    public double getPhysique() {
        return physique;
    }

    public void setPhysique(double physique) {
        this.physique = physique;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getAgility() {
        return agility;
    }

    public void setAgility(double agility) {
        this.agility = agility;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(double maxHp) {
        this.maxHp = maxHp;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getDefence() {
        return defence;
    }

    public void setDefence(double defence) {
        this.defence = defence;
    }

    public double getStrike() {
        return strike;
    }

    public void setStrike(double strike) {
        this.strike = strike;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDodge() {
        return dodge;
    }

    public void setDodge(double dodge) {
        this.dodge = dodge;
    }

    public double getLuck() {
        return luck;
    }

    public void setLuck(double luck) {
        this.luck = luck;
    }

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
     * 1敏捷 = 3速度 + 1爆率 + 1闪避
     */
    private void applyAgility(double agility) {
        this.speed += agility * 3;
        this.strike += agility * 1;
        this.dodge += dodge * 1;
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
            this.dodge += properties.dodge;
            this.luck += properties.luck;
        }
    }

    /**
     * 返回当前属性的非0值变动
     */
    public String calChangeValueLog() {
        StringBuilder sb = new StringBuilder();
        if (physique != 0) {
            sb.append("体质: ").append(physique > 0 ? "+" : "-").append(physique).append(";");
        }
        if (strength != 0) {
            sb.append("力量: ").append(strength > 0 ? "+" : "-").append(strength).append(";");
        }
        if (agility != 0) {
            sb.append("敏捷: ").append(agility > 0 ? "+" : "-").append(agility).append(";");
        }
        if (maxHp != 0) {
            sb.append("血量上限: ").append(maxHp > 0 ? "+" : "-").append(maxHp).append(";");
        }
        if (hp != 0) {
            sb.append("当前血量: ").append(hp > 0 ? "+" : "-").append(hp).append(";");
        }
        if (attack != 0) {
            sb.append("攻击力: ").append(attack > 0 ? "+" : "-").append(attack).append(";");
        }
        if (defence != 0) {
            sb.append("防御力: ").append(defence > 0 ? "+" : "-").append(defence).append(";");
        }
        if (strike != 0) {
            sb.append("暴击率: ").append(strike > 0 ? "+" : "-").append(strike).append(";");
        }
        if (speed != 0) {
            sb.append("速度: ").append(speed > 0 ? "+" : "-").append(speed).append(";");
        }
        if (dodge != 0) {
            sb.append("闪避: ").append(dodge > 0 ? "+" : "-").append(dodge).append(";");
        }
        if (luck != 0) {
            sb.append("幸运: ").append(luck > 0 ? "+" : "-").append(luck).append(";");
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return "属性未发生任何变化";
    }

    public boolean isEmpty() {
        return physique == 0 &&
                strength == 0 &&
                agility == 0 &&
                maxHp == 0.0 &&
                hp == 0.0 &&
                attack == 0.0 &&
                defence == 0.0 &&
                strike == 0.0 &&
                speed == 0.0 &&
                dodge == 0.0 &&
                luck == 0.0;

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
        dodge = 0.0;
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
        public PropertiesBuilder setDodge(double dodge) {
            template.dodge = dodge;
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
