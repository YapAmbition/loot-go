package com.nikfce.stage;

import com.alibaba.fastjson.JSON;
import com.nikfce.role.Looter;
import com.nikfce.thread.ThreadLocalMap;
import com.nikfce.util.CollectionUtil;
import java.util.List;
import java.util.stream.Collectors;

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
        ThreadLocalMap.getRecorder().record("==============================");
        ThreadLocalMap.getRecorder().record("战斗开始!");
        // 调用运动员的战斗开始钩子
        callBattleStart();

        // 介绍运动员的属性
        introduceLooter();
        BattleFinishEnum battleFinishEnumState = battleFinishState();
        while (!battleFinishEnumState.battleFinish) {
            roundCount ++;
            if (roundCount > 999) {
                throw new RuntimeException("回合数超限,一定是出了什么问题");
            }
            ThreadLocalMap.getRecorder().record("------------------------------");
            ThreadLocalMap.getRecorder().record("开始第[" + roundCount + "]回合");
            // 找到下次行动的角色
            Looter currentLooter = actionPlan.next();
            ThreadLocalMap.getRecorder().record_f("当前攻击的角色为: [%s]", currentLooter.getName());
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
        ThreadLocalMap.getRecorder().record_f(">>>战斗结束,共[%s]回合<<<", roundCount);
        boolean result = handleBattleFinish(battleFinishEnumState);
        ThreadLocalMap.getRecorder().record("==============================");
        return result;
    }

    /**
     * 介绍looter的属性
     */
    private void introduceLooter() {
        for (Looter looter : players) {
            ThreadLocalMap.getRecorder().record_f("[%s]: %s", looter.getName(), looter.currentPropertiesSummary());
        }
        for (Looter looter : enemies) {
            ThreadLocalMap.getRecorder().record_f("[%s]: %s", looter.getName(), looter.currentPropertiesSummary());
        }
    }

    /**
     * 调用looter的战斗结束钩子
     */
    private void callBattleEnd() {
        for (Looter looter : players) {
            looter.battleEnd();
        }
        for (Looter looter : enemies) {
            looter.battleEnd();
        }
    }

    /**
     * 调用looter的战斗开始钩子
     */
    private void callBattleStart() {
        for (Looter looter : players) {
            looter.battleStart();
        }
        for (Looter looter : enemies) {
            looter.battleStart();
        }
    }

    /**
     * 检查looter的血量状态
     */
    private void checkLooterStatus() {
        for (Looter looter : players) {
            ThreadLocalMap.getRecorder().record_f("[%s] : (%s/%s)", looter.getName(), looter.currentHp(), looter.currentMaxHp());
        }
        for (Looter looter : enemies) {
            ThreadLocalMap.getRecorder().record_f("[%s] : (%s/%s)", looter.getName(), looter.currentHp(), looter.currentMaxHp());
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
            case PLAYER_ALL_DEAD:
                ThreadLocalMap.getRecorder().record_f("[%s]获胜", JSON.toJSONString(enemies.stream().map(Looter::getName).collect(Collectors.toList())));
                return false;
            case ENEMIES_ALL_DEAD:
                ThreadLocalMap.getRecorder().record_f("[%s]获胜", JSON.toJSONString(players.stream().map(Looter::getName).collect(Collectors.toList())));
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
        for (Looter looter : looterList) {
            if (!looter.isDead()) {
                return false;
            }
        }
        return true;
    }

}
