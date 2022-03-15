package com.nikfce.http.dto;

import com.nikfce.scene.Scene;
import lombok.Data;

/**
 * @author shenzhencheng 2022/3/14
 */
@Data
public class SceneDTO {

    private String name;
    private String desc;

    public static SceneDTO generateByScene(Scene scene) {
        if (scene == null) return null;
        SceneDTO sceneDTO = new SceneDTO();
        sceneDTO.setName(scene.getName());
        sceneDTO.setDesc(scene.getDesc());
        return sceneDTO;
    }

}
