package com.nikfce.action;

import com.nikfce.role.Looter;
import com.nikfce.stage.RoundLifecycle;

import java.util.List;

/**
 * 技能使用的上下文
 * @author shenzhencheng 2022/3/9
 */
public class SkillContext {


    public final RoundLifecycle roundLifecycle;
    public final Looter user;
    public final List<Looter> enemy;
    /**
     * 这是技能造成的实际影响,选技能和技能生效时不要用这个字段
     */
    public final Effect actualEffect;

    public SkillContext(RoundLifecycle roundLifecycle, Looter user, List<Looter> enemy, Effect effect) {
        this.roundLifecycle = roundLifecycle;
        this.user = user;
        this.enemy = enemy;
        this.actualEffect = effect;
    }

}
