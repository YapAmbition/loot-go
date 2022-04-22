package com.nikfce.config;

import lombok.Data;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * @author shenzhencheng 2022/3/12
 */
@Data
public class LootConfig {

    /**
     * 场景定义的目录
     */
    private String sceneDir;
    /**
     * 动态配置looter的配置文件目录
     */
    private String looterDir;
    /**
     * 用户token在cookie中的key
     */
    private String tokenCookieKey = "loot-user-token";











    private LootConfig() {}

    private static LootConfig INSTANCE ;

    public static LootConfig getInstance() {
        if (INSTANCE == null) {
            throw new RuntimeException("未执行配置初始化");
        }
        return INSTANCE;
    }

    /**
     * 初始化配置
     */
    public synchronized static void init() {
        URL url = LootConfig.class.getClassLoader().getResource("loot.yml");
        try(FileReader fr = new FileReader(url.getFile())) {
            Yaml yaml = new Yaml(new Constructor(LootConfig.class));
            INSTANCE = yaml.loadAs(fr, LootConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("初始化配置文件失败", e);
        }
    }

}
