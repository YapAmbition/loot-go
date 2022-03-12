package com.nikfce.scene;

import com.alibaba.fastjson.JSON;
import com.nikfce.config.LootConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenzhencheng 2022/3/12
 */
public class SceneParser {

    private static final Logger LOG = LoggerFactory.getLogger(SceneParser.class);

    /**
     * sample.yml作为示例不做解析
     */
    private static final String SAMPLE_NAME = "sample.yml";

    /**
     * 解析所有的场景
     * @return {sceneName: scene}
     */
    public static List<Scene> parseAll() {
        List<Scene> result = new ArrayList<>();
        String sceneDir = LootConfig.getInstance().getSceneDir();
        File file = new File(sceneDir);
        if (file.exists() && file.isDirectory()) {
            String[] filenames = file.list();
            if (filenames == null) {
                return result;
            }
            for (String filename : filenames) {
                if (!filename.endsWith(".yml")) {
                    continue;
                }
                if (SAMPLE_NAME.equalsIgnoreCase(filename)) {
                    continue;
                }
                try {
                    Scene scene = parse(String.format("%s/%s", sceneDir, filename));
                    scene.check();
                    result.add(scene);
                } catch (FileNotFoundException e) {
                    LOG.warn("解析场景失败: {}", filename, e);
                }
            }
        }
        return result;
    }

    /**
     * 将某特定文件解析为场景
     */
    public static Scene parse(String filePath) throws FileNotFoundException {
        LOG.info("开始解析场景: " + filePath);
        FileReader fr = new FileReader(filePath.trim());
        Yaml yaml = new Yaml(new Constructor(Scene.class));
        return yaml.loadAs(fr, Scene.class);
    }

}
