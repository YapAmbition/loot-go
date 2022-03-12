package com.nikfce.util;

/**
 * @author shenzhencheng 2022/3/11
 */
public class StringUtil {

    public static boolean isEmpty(String ss) {
        return ss == null || ss.trim().length() == 0;
    }

    public static boolean isNotEmpty(String ss) {
        return !isEmpty(ss);
    }

}
