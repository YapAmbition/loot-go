package com.nikfce.stage.competion;

import com.nikfce.recoder.BufferRecorder;
import com.nikfce.role.Looter;
import com.nikfce.role.hero.LiKun;
import com.nikfce.role.hero.YongGe;
import com.nikfce.stage.Battle;
import com.nikfce.thread.ThreadLocalMap;

import java.util.Collections;

/**
 * @author shenzhencheng 2022/3/11
 */
public class OneVsOne {

    private static Looter sportMan1;
    private static Looter sportMan2;

    private static void initSportMan() {
        sportMan1 = new YongGe();
        sportMan2 = new LiKun();
    }

    private static void oneVsOne() {
        initSportMan();

        ThreadLocalMap.getRecorder().record_f("=====================");
        ThreadLocalMap.getRecorder().record_f("下面将由%s与%s进行1V1比赛", sportMan1.name, sportMan2.name);
        Battle battle = new Battle(Collections.singletonList(sportMan1), Collections.singletonList(sportMan2));
        boolean win = battle.battleStart();
        Looter winner = win ? sportMan1 : sportMan2;
        ThreadLocalMap.getRecorder().record_f("=====================");
        ThreadLocalMap.getRecorder().record_f("战斗结束," + winner.name + "获得最终胜利");
    }

    public static void main(String[] args) {
        BufferRecorder recorder = new BufferRecorder();
        ThreadLocalMap.setRecorder(recorder);
        oneVsOne();
        System.out.println(recorder.flush());
    }

}
