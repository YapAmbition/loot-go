package com.nikfce.recoder;

/**
 * 记录者接口
 * @author shenzhencheng 2022/3/11
 */
public interface Recorder {

    /**
     * 记录单个文本
     */
    void record(String msg);

    /**
     * 格式化记录
     */
    void record_f(String format, Object...args);

}
