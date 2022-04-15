package com.nikfce.http.request;

import lombok.Data;

import java.util.List;

/**
 * 进入Flow之后的结果
 * @author shenzhencheng 2022/3/15
 */
@Data
public class FlowResponse {

    private boolean win;
    // 剩余几条命
    private int remainHealth;
    private List<String> logs;

}
