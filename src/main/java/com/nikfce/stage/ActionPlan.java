package com.nikfce.stage;

import com.nikfce.role.Looter;

/**
 * @author shenzhencheng 2022/3/2
 */
public interface ActionPlan {

    /**
     * 返回下一个该行动的looter
     */
    Looter next();

}
