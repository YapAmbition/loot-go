package com.nikfce.recoder;

import java.util.HashSet;
import java.util.Set;

/**
 * 带buffer的记录器,buffer大小需要自己控制.如果超过buffer长度之后还在record,那么会把之前的记录清空
 * @author shenzhencheng 2022/3/11
 */
public class BufferRecorder implements Recorder {

    // 最大的buffer长度(2M),超过这个长度自动会自动清空buffer
    private int maxBufferLength = 2 * 1024 * 1024;

    private final Set<BufferRecorderListener> listeners = new HashSet<>();

    private StringBuilder buffer = new StringBuilder();

    public int getMaxBufferLength() {
        return maxBufferLength;
    }

    public void setMaxBufferLength(int maxBufferLength) {
        this.maxBufferLength = maxBufferLength;
    }

    public void addListener(BufferRecorderListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(BufferRecorderListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    @Override
    public void record(String msg) {
        buffer.append(msg).append('\n');
        if (buffer.length() > maxBufferLength) {
            notifyAllListener();
            clearBuffer();
        }
    }

    @Override
    public void record_f(String format, Object... args) {
        record(String.format(format, args));
    }

    public String flush() {
        String ss = buffer.toString();
        clearBuffer();
        return ss;
    }

    public int currentBufferLength() {
        return buffer.length();
    }

    private void clearBuffer() {
        buffer = new StringBuilder();
    }

    private void notifyAllListener() {
        String ss = buffer.toString();
        for (BufferRecorderListener listener : listeners) {
            listener.onOverBufferLength(ss);
        }
    }


    interface BufferRecorderListener {

        /**
         * 当buffer的长度超过限制时,会调用该接口并清空buffer
         */
        void onOverBufferLength(String msg);

    }

}
