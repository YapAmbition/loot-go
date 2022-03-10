package com.nikfce.stage;

import com.nikfce.role.Looter;
import com.nikfce.role.hero.*;

import java.util.Collections;
import java.util.List;

/**
 * @author shenzhencheng 2022/3/10
 */
public class Stage {

    private static List<Looter> createPlayers() {
        return Collections.singletonList(new YaQiang());
    }

    private static List<Looter> createEnemies() {
        return Collections.singletonList(new TaoZi());
    }

    public static void main(String[] args) {
        // 玩家
        List<Looter> players = createPlayers();
        // 敌人
        List<Looter> enemies = createEnemies();
        // 创建战斗
        Battle battle = new Battle(players, enemies);
        battle.battleStart();
    }

}
