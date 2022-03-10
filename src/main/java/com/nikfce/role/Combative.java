package com.nikfce.role;

import com.nikfce.action.Effect;
import com.nikfce.stage.RoundContext;

/**
 * @author shenzhencheng 2022/3/9
 */
public interface Combative {

    /**
     * 回合开始
     */
    void roundStart(RoundContext roundContext);

    /**
     * 攻击接口
     */
    void attack(RoundContext roundContext);

    /**
     * 被攻击接口
     */
    void beAttack(Looter attacker, Effect effect);

    /**
     * 攻击结束接口,这个接口理论上是被攻击者调用的,所以第一个参数应该是被攻击者填this即可,由于攻击可能会被被攻击者化解,所以这里的Effect是实际造成的影响
     * @param target 被攻击者
     * @param actualEffect 实际造成的影响
     */
    void attackFinish(Looter target, Effect actualEffect);

    /**
     * 回合结束
     */
    void roundEnd(RoundContext roundContext);

}
