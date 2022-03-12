package com.nikfce.register;

import com.alibaba.fastjson.JSON;
import com.nikfce.scene.Scene;
import com.nikfce.scene.SceneParser;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 场景注册器,用来注册生成的场景,并通过传入name来返回对应场景
 * @author shenzhencheng 2022/3/12
 */
public class SceneRegisterCenter {

    private static final Map<String, Scene> SCENE_MAP = new ConcurrentHashMap<>();

    /**
     * 注册场景
     */
    public synchronized static void register(Scene scene) {
        if (scene != null) {
            SCENE_MAP.put(scene.getName(), scene);
        }
    }

    /**
     * 展示所有的场景名
     */
    public static Set<String> showSceneList() {
        return SCENE_MAP.keySet();
    }

    /**
     * 根据传入的场景名字,生成一个全新的场景
     * @param sceneName 场景名
     */
    public static Scene generateScene(String sceneName) {
        Scene scene = SCENE_MAP.get(sceneName);
        if (scene == null) {
            throw new RuntimeException("没有注册该场景");
        }
        return JSON.parseObject(JSON.toJSONString(scene), Scene.class);
    }

    /**
     * 从场景配置文件中加载并注册所有的场景
     */
    public static void registerSceneFromConfig() {
        List<Scene> sceneList = SceneParser.parseAll();
        for (Scene scene : sceneList) {
            register(scene);
        }
    }

}
