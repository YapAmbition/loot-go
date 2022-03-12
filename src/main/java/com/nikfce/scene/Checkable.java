package com.nikfce.scene;

/**
 * @author shenzhencheng 2022/3/12
 */
public interface Checkable {

    /**
     * 对自己做合法性检查,如果检查不通过直接抛错
     */
    void check() throws RuntimeException;

}
