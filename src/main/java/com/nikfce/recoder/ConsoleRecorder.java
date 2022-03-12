package com.nikfce.recoder;

/**
 * 命令行记录,这个用单例就好,所有线程都用这一个记录器
 * @author shenzhencheng 2022/3/11
 */
public class ConsoleRecorder implements Recorder {

    private static final ConsoleRecorder ConsoleRecorder = new ConsoleRecorder();

    private ConsoleRecorder() {}

    public static ConsoleRecorder getInstance() {
        return ConsoleRecorder;
    }

    @Override
    public void record(String msg) {
        System.out.println(msg);
    }

    @Override
    public void record_f(String format, Object... args) {
        record(String.format(format, args));
    }
}
