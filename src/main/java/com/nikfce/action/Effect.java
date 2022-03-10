package com.nikfce.action;

import com.nikfce.role.Properties;

/**
 * 影响,包含了属性,战斗数值的改变
 * @author shenzhencheng 2022/3/9
 */
public class Effect {

    public final Properties properties;
    // 本次影响是否产生了暴击
    public boolean strike = false;

    public Effect(Properties properties) {
        this.properties = properties;
    }

    public Effect(Properties properties, boolean strike) {
        this.properties = properties;
        this.strike = strike;
    }

}
