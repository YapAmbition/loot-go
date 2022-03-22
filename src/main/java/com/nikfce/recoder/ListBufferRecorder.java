package com.nikfce.recoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用list存储的bufferRecorder
 * flush时直接返回列表,方便前端展示
 * @author shenzhencheng 2022/3/16
 */
public class ListBufferRecorder implements Recorder {

    // buffer列表最大条数
    private int maxBufferSize = 2500;

    private final List<String> buffer = new ArrayList<>();

    private final Set<ListBufferRecorder.ListBufferRecorderListener> listeners = new HashSet<>();

    public int getMaxBufferSize() {
        return maxBufferSize;
    }

    public void setMaxBufferSize(int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
    }

    @Override
    public void record(String msg) {
        buffer.add(msg);
        if (buffer.size() > maxBufferSize) {
            notifyAllListener();
            clearBuffer();
        }
    }

    /**
     * 通知监听者,要清空buffer了
     */
    private void notifyAllListener() {
        List<String> msg = flush();
        for (ListBufferRecorderListener listener : listeners) {
            listener.onOverBufferSize(msg);
        }
    }

    @Override
    public void record_f(String format, Object... args) {
        record(String.format(format, args));
    }

    public List<String> flush() {
        List<String> result = new ArrayList<>(buffer);
        clearBuffer();
        return result;
    }

    public int getCurrentBufferSize() {
        return buffer.size();
    }

    private synchronized void clearBuffer() {
        buffer.clear();
    }

    interface ListBufferRecorderListener {
        /**
         * 当buffer的长度超过限制时,会调用该接口并清空buffer
         */
        void onOverBufferSize(List<String> msgs);
    }
}
