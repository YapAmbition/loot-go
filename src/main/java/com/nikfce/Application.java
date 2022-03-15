package com.nikfce;

import com.nikfce.config.LootConfig;
import com.nikfce.register.LooterRegisterCenter;
import com.nikfce.register.SceneRegisterCenter;
import com.nikfce.register.SkillRegisterCenter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shenzhencheng 2022/3/1
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        LootConfig.init();
        SkillRegisterCenter.registerSkillFromSrc();
        LooterRegisterCenter.registerLooterFromConfig();
        SceneRegisterCenter.registerSceneFromConfig();

        SpringApplication.run(Application.class, args);
    }

}
