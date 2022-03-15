package com.nikfce.http.request;

import lombok.Data;

/**
 * 进入Flow之后的结果
 * @author shenzhencheng 2022/3/15
 */
@Data
public class FlowResponse {

    private boolean isWin;
    private String log;

}
