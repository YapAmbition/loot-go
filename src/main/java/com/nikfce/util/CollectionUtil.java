package com.nikfce.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author shenzhencheng 2022/3/2
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 合并所有列表,返回一个新列表
     */
    public static <T> List<T> mergeLists(List<T> ... lists) {
        List<T> result = new ArrayList<>();
        for (List<T> list : lists) {
            result.addAll(list);
        }
        return result;
    }

}
