package com.nikfce.thread;

import com.nikfce.recoder.ConsoleRecorder;
import com.nikfce.recoder.Recorder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenzhencheng 2022/3/11
 */
public class ThreadLocalMap {

    private static final String TL_RECORDER = "thread-recorder";

    private static final ThreadLocal<Map<Object, Object>> ThreadContext = new MapThreadLocal();

    public static void put(Object key, Object value) {
        ThreadContext.get().put(key, value);
    }

    public static Object get(Object key) {
        return ThreadContext.get().get(key);
    }

    public static Recorder getRecorder() {
        return (Recorder) get(TL_RECORDER);
    }

    public static void setRecorder(Recorder recorder) {
        if (recorder != null) {
            put(TL_RECORDER, recorder);
        }
    }

    private static class MapThreadLocal extends ThreadLocal<Map<Object, Object>> {
        @Override
        protected Map<Object, Object> initialValue() {
            Map<Object, Object> map = new HashMap<>();
            map.put(TL_RECORDER, ConsoleRecorder.getInstance());
            return map;
        }
    }

}
