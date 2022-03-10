package com.nikfce.stage;

import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author shenzhencheng 2022/3/10
 */
public class Stage {

    private static List<Looter> createPlayers() {
        Properties properties = Properties.PropertiesBuilder.create()
                .setPhysique(35)
                .setStrength(30)
                .setAgility(5)
                .setApplyAttribute(true)
                .build();
        Looter looter = new Looter("李坤", properties);
        return Collections.singletonList(looter);
    }

    private static List<Looter> createEnemies() {
        Properties xlProperties = Properties.PropertiesBuilder.create()
                .setPhysique(35)
                .setStrength(20)
                .setAgility(2)
                .setApplyAttribute(true)
                .build();
        Looter xl = new Looter("谢力", xlProperties);

        Properties ypProperties = Properties.PropertiesBuilder.create()
                .setPhysique(30)
                .setStrength(28)
                .setAgility(5)
                .setApplyAttribute(true)
                .build();
        Looter yp = new Looter("杨胖", ypProperties);

        Properties tzProperties = Properties.PropertiesBuilder.create()
                .setPhysique(40)
                .setStrength(24)
                .setAgility(1)
                .setApplyAttribute(true)
                .build();
        Looter tz = new Looter("陶子", tzProperties);

        return Arrays.asList(xl, yp, tz);
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
