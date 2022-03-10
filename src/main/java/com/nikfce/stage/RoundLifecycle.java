package com.nikfce.stage;

/**
 * @author shenzhencheng 2022/3/9
 */
public enum RoundLifecycle {

    ROUND_START, // 回合开始
    BEFORE_ATTACK, // 攻击之前
    BEFORE_BE_ATTACK, // 被攻击之前
    AFTER_BE_ATTACK, // 被攻击之后
    AFTER_ATTACK, // 攻击之后
    ROUND_END, // 回合结束

}
