package com.nikfce.stage;

import com.alibaba.fastjson.JSON;
import com.nikfce.role.Looter;
import com.nikfce.role.hero.*;

import java.util.*;

/**
 * @author shenzhencheng 2022/3/10
 */
public class Stage {

    /**
     * 积分赛
     */
    private static void pointRace() {
        Map<Looter, Integer> heroMap = new HashMap<>();
        heroMap.put(new LiKun(), 0);
        heroMap.put(new TaoZi(), 0);
        heroMap.put(new XieLi(), 0);
        heroMap.put(new YangPang(), 0);
        heroMap.put(new YaQiang(), 0);

        for (Looter looter : heroMap.keySet()) {
            for (Looter target : heroMap.keySet()) {
                if (looter == target) {
                    continue;
                }
                System.out.println("=====================");
                System.out.println("下面由" + looter.name + "对战" + target.name);
                System.out.println("=====================");
                Battle battle = new Battle(Collections.singletonList(looter), Collections.singletonList(target));
                boolean win = battle.battleStart();
                if (win) {
                    heroMap.put(looter, heroMap.get(looter) + 1);
                }
            }
        }

        System.out.println("=====================");
        System.out.println("所有对战结束,展示对战结果");
        for (Looter looter : heroMap.keySet()) {
            System.out.println(looter.name + ": " + heroMap.get(looter));
        }
    }

    /**
     * 1 V 1
     */
    private static void oneVsOne(Looter one, Looter two) {
        System.out.println("=====================");
        System.out.printf("下面将由%s与%s进行1V1比赛%n", one.name, two.name);
        Battle battle = new Battle(Collections.singletonList(one), Collections.singletonList(two));
        boolean win = battle.battleStart();
        Looter winner = win ? one : two;
        System.out.println("=====================");
        System.out.println("战斗结束," + winner.name + "获得最终胜利");
    }

    public static void main(String[] args) {
        oneVsOne(new LiKun(), new YangPang());
    }

}
