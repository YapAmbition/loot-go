package com.nikfce.stage.competion;

import com.nikfce.config.LootConfig;
import com.nikfce.register.LooterRegisterCenter;
import com.nikfce.register.SkillRegisterCenter;
import com.nikfce.role.Looter;
import com.nikfce.stage.Battle;
import com.nikfce.thread.ThreadLocalMap;

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
        scoreMap.put(LooterRegisterCenter.generateLooter("LOOTER_1"), 0);
        scoreMap.put(LooterRegisterCenter.generateLooter("LOOTER_2"), 0);
        scoreMap.put(LooterRegisterCenter.generateLooter("LOOTER_3"), 0);
        scoreMap.put(LooterRegisterCenter.generateLooter("LOOTER_4"), 0);
        scoreMap.put(LooterRegisterCenter.generateLooter("LOOTER_5"), 0);
        scoreMap.put(LooterRegisterCenter.generateLooter("LOOTER_6"), 0);
    }

    private static void gameStart() {
        initSportMan();
        for (Looter looter : scoreMap.keySet()) {
            for (Looter target : scoreMap.keySet()) {
                if (looter == target) {
                    continue;
                }
                ThreadLocalMap.getRecorder().record_f("=====================");
                ThreadLocalMap.getRecorder().record_f("下面由" + looter.getName() + "对战" + target.getName());
                ThreadLocalMap.getRecorder().record_f("=====================");
                Battle battle = new Battle(Collections.singletonList(looter), Collections.singletonList(target));
                boolean win = battle.battleStart();
                if (win) {
                    scoreMap.put(looter, scoreMap.get(looter) + 1);
                } else {
                    scoreMap.put(target, scoreMap.get(target) + 1);
                }
            }
        }

        ThreadLocalMap.getRecorder().record_f("=====================");
        ThreadLocalMap.getRecorder().record_f("所有对战结束,展示对战结果");
        for (Looter looter : scoreMap.keySet()) {
            ThreadLocalMap.getRecorder().record_f(looter.getName() + ": " + scoreMap.get(looter));
        }
    }

    public static void main(String[] args) {
        LootConfig.init();
        SkillRegisterCenter.registerSkillFromSrc();
        LooterRegisterCenter.registerLooterFromConfig();

        gameStart();
    }

}
