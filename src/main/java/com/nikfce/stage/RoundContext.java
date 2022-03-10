package com.nikfce.stage;

import com.nikfce.role.Looter;

import java.util.List;

/**
 * @author shenzhencheng 2022/3/9
 */
public class RoundContext {

    public int round;
    public final List<Looter> targets;

    public RoundContext(int round, List<Looter> targets) {
        this.round = round;
        this.targets = targets;
    }

    public RoundContext(List<Looter> targets) {
        this.targets = targets;
    }
}
