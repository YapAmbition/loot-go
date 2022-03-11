package com.nikfce.stage.competion;

import com.nikfce.role.Looter;
import com.nikfce.role.hero.*;
import com.nikfce.stage.Battle;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 积分赛,各个运动员互相打,赢的+1分
 * @author shenzhencheng 2022/3/11
 */
public class PointRace {

    private static final Map<Looter, Integer> scoreMap = new HashMap<>();

    private static void initSportMan() {
        scoreMap.put(new LiKun(), 0);
        scoreMap.put(new TaoZi(), 0);
        scoreMap.put(new XieLi(), 0);
        scoreMap.put(new YangPang(), 0);
        scoreMap.put(new YaQiang(), 0);
        scoreMap.put(new YongGe(), 0);
    }

    private static void gameStart() {
        initSportMan();
        for (Looter looter : scoreMap.keySet()) {
            for (Looter target : scoreMap.keySet()) {
                if (looter == target) {
                    continue;
                }
                System.out.println("=====================");
                System.out.println("下面由" + looter.name + "对战" + target.name);
                System.out.println("=====================");
                Battle battle = new Battle(Collections.singletonList(looter), Collections.singletonList(target));
                boolean win = battle.battleStart();
                if (win) {
                    scoreMap.put(looter, scoreMap.get(looter) + 1);
                } else {
                    scoreMap.put(target, scoreMap.get(target) + 1);
                }
            }
        }

        System.out.println("=====================");
        System.out.println("所有对战结束,展示对战结果");
        for (Looter looter : scoreMap.keySet()) {
            System.out.println(looter.name + ": " + scoreMap.get(looter));
        }
    }

    public static void main(String[] args) {
        gameStart();
    }

}
