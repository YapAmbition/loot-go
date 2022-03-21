package com.nikfce.http.dto;

import lombok.Data;

import java.util.List;

/**
 * 下一步返回的东西
 * 根据UserSpace的currentScene是否为空判断该用户当前是否有在哪个场景里面,从而展示不同的数据
 * @author shenzhencheng 2022/3/14
 */
@Data
public class NextDto {

    /**
     * 类型: scene/flow/looter/lose/allClear
     * 解释: 选择场景/选择关卡/选择looter/失败/通关
     */
    private String type;
    private String desc;
    private List<SceneDTO> sceneList;
    private List<FlowDTO> flowList;
    private List<LooterDTO> looterList;

}
