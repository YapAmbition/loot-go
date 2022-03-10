package com.nikfce.stage;

import com.nikfce.role.Looter;
import com.nikfce.util.CollectionUtil;
import java.util.List;

/**
 * @author shenzhencheng 2022/3/1
 */
public class Battle {

    // 玩家
    private final List<Looter> players;
    // 敌人
    private final List<Looter> enemies;
    // 行动计划
    private final ActionPlan actionPlan;
    // 回合数,只要有人行动了,回合数就增加
    private int roundCount = 0;

    public Battle(List<Looter> players, List<Looter> enemies) {
        this.players = players;
        this.enemies = enemies;
        //noinspection unchecked
        actionPlan = new BottleActionPlan(CollectionUtil.mergeLists(players, enemies));
    }

    public void battleStart() {
        BattleFinishEnum battleFinishEnumState = battleFinishState();
        while (!battleFinishEnumState.battleFinish) {
            roundCount ++;
            if (roundCount > 999) {
                throw new RuntimeException("回合数超限,一定是出了什么问题");
            }
            // 找到下次行动的角色
            Looter currentLooter = actionPlan.next();
            // 角色
            RoundContext roundContext;
            if (isPlayer(currentLooter)) {
                roundContext = new RoundContext(roundCount, enemies);
            } else {
                roundContext = new RoundContext(roundCount, players);
            }
            currentLooter.attack(roundContext);

            battleFinishEnumState = battleFinishState();
        }
        handleBattleFinish(battleFinishEnumState);
    }

    /**
     * 判断当前角色是不是玩家控制的角色
     */
    private boolean isPlayer(Looter looter) {
        return players.contains(looter);
    }

    private void handleBattleFinish(BattleFinishEnum battleFinishEnum) {
        switch (battleFinishEnum) {
            case ALL_DEAD:
                System.out.println("双方同归于尽了");
                break;
            case PLAYER_ALL_DEAD:
                System.out.println("player死完了");
                break;
            case ENEMIES_ALL_DEAD:
                System.out.println("enemies死完了");
                break;
        }
    }



    /**
     * 判断战斗是否结束
     */
    private BattleFinishEnum battleFinishState() {
        if (allDead(players)) {
            if (allDead(enemies)) {
                return BattleFinishEnum.ALL_DEAD;
            } else {
                return BattleFinishEnum.PLAYER_ALL_DEAD;
            }
        } else {
            if (allDead(enemies)) {
                return BattleFinishEnum.ENEMIES_ALL_DEAD;
            } else {
                return BattleFinishEnum.NOPE;
            }
        }
    }

    private boolean allDead(List<Looter> looterList) {
        for (int i = 0 ; i < looterList.size() ; i ++) {
            if (!looterList.get(i).isDead()) {
                return false;
            }
        }
        return true;
    }

}
