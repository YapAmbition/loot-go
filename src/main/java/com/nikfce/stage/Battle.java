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

    public boolean battleStart() {
        System.out.println("战斗开始!");
        callBattleStart();
        BattleFinishEnum battleFinishEnumState = battleFinishState();
        while (!battleFinishEnumState.battleFinish) {
            roundCount ++;
            if (roundCount > 999) {
                throw new RuntimeException("回合数超限,一定是出了什么问题");
            }
            System.out.println("------------------------------");
            System.out.println("开始第" + roundCount + "回合");
            // 找到下次行动的角色
            Looter currentLooter = actionPlan.next();
            System.out.println("当前攻击的角色为:" + currentLooter.name);
            // 角色
            RoundContext roundContext;
            if (isPlayer(currentLooter)) {
                roundContext = new RoundContext(roundCount, enemies);
            } else {
                roundContext = new RoundContext(roundCount, players);
            }
            currentLooter.attack(roundContext);
            checkLooterStatus();

            battleFinishEnumState = battleFinishState();
        }
        callBattleEnd();
        System.out.println("第" + roundCount + "回合,战斗结束");
        return handleBattleFinish(battleFinishEnumState);
    }

    private void callBattleEnd() {
        for (Looter looter : players) {
            looter.battleEnd();
        }
        for (Looter looter : enemies) {
            looter.battleEnd();
        }
    }

    private void callBattleStart() {
        for (Looter looter : players) {
            looter.battleStart();
        }
        for (Looter looter : enemies) {
            looter.battleStart();
        }
    }

    private void checkLooterStatus() {
        for (Looter looter : players) {
            System.out.printf("[%s] : (%s/%s)%n", looter.name, looter.currentHp(), looter.currentMaxHp());
        }
        for (Looter looter : enemies) {
            System.out.printf("[%s] : (%s/%s)%n", looter.name, looter.currentHp(), looter.currentMaxHp());
        }
    }

    /**
     * 判断当前角色是不是玩家控制的角色
     */
    private boolean isPlayer(Looter looter) {
        return players.contains(looter);
    }

    private boolean handleBattleFinish(BattleFinishEnum battleFinishEnum) {
        switch (battleFinishEnum) {
            case ALL_DEAD:
                System.out.println("双方同归于尽了");
                return true;
            case PLAYER_ALL_DEAD:
                System.out.println("player死完了");
                return false;
            case ENEMIES_ALL_DEAD:
                System.out.println("enemies死完了");
                return true;
            default:
                throw new RuntimeException("未知的结束状态:" + battleFinishEnum);
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
