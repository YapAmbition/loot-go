package com.nikfce.register;

import com.nikfce.scene.Scene;
import com.nikfce.scene.SceneParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 地图注册器,用来注册生成的地图,并通过传入name来返回对应地图
 * @author shenzhencheng 2022/3/12
 */
public class SceneRegisterCenter {

    private static final Logger LOG = LoggerFactory.getLogger(SceneRegisterCenter.class);

    private static final Map<String, Scene> SCENE_MAP = new ConcurrentHashMap<>();

    /**
     * 注册地图
     */
    public synchronized static void register(Scene scene) {
        if (scene != null) {
            SCENE_MAP.put(scene.getName(), scene);
            LOG.info("成功注册地图: {}", scene.getName());
        }
    }

    /**
     * 展示所有的地图名
     */
    public static Set<String> showSceneList() {
        return SCENE_MAP.keySet();
    }

    /**
     * 根据传入的地图名字,生成一个全新的地图
     * @param sceneName 地图名
     */
    public static Scene generateScene(String sceneName) {
        Scene scene = SCENE_MAP.get(sceneName);
        if (scene == null) {
            throw new RuntimeException("没有注册该地图");
        }
        return scene.snapshot();
    }

    /**
     * 从地图配置文件中加载并注册所有的地图
     */
    public static void registerSceneFromConfig() {
        List<Scene> sceneList = SceneParser.parseAll();
        for (Scene scene : sceneList) {
            register(scene);
        }
    }

}
