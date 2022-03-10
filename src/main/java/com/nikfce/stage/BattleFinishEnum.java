package com.nikfce.stage;

/**
 * @author shenzhencheng 2022/3/1
 */
public enum BattleFinishEnum {
    NOPE(false),//没有一方死完
    PLAYER_ALL_DEAD(true),//player死完了
    ENEMIES_ALL_DEAD(true),//enemies死完了
    ALL_DEAD(true);//双方都死完了

    public final boolean battleFinish;
    BattleFinishEnum(boolean battleFinish) {
        this.battleFinish = battleFinish;
    }
}
