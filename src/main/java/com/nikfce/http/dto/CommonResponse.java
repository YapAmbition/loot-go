package com.nikfce.http.dto;

import lombok.Data;

/**
 * @author shenzhencheng 2022/3/21
 */
@Data
public class CommonResponse<T> {

    private int code; // 返回码
    private String type; // 响应类型
    private String errMsg; // 错误信息
    private T data; // 响应数据

    public static class CommonResponseBuilder<T> {
        private final CommonResponse<T> template;

        public CommonResponseBuilder() {
            template = new CommonResponse<T>();
        }

        public CommonResponseBuilder<T> setCode(int code) {
            template.setCode(code);
            return this;
        }

        public CommonResponseBuilder<T> setType(String type) {
            template.setType(type);
            return this;
        }

        public CommonResponseBuilder<T> setErrMsg(String errMsg) {
            template.setErrMsg(errMsg);
            return this;
        }

        public CommonResponseBuilder<T> setData(T data) {
            template.setData(data);
            return this;
        }

        public CommonResponse<T> build() {
            CommonResponse<T> commonResponse = new CommonResponse<>();
            commonResponse.setCode(template.getCode());
            commonResponse.setType(template.getType());
            commonResponse.setErrMsg(template.getErrMsg());
            commonResponse.setData(template.getData());
            return commonResponse;
        }
    }

}
