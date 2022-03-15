package com.nikfce.http.user;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author shenzhencheng 2022/3/14
 */
public class TokenGenerator {

    private static final AtomicLong casToken = new AtomicLong(-1);
    private static final AtomicInteger counterPerMill = new AtomicInteger(0);

    /**
     * 1ms生成1000个id
     */
    public static String nextToken() {
        long lastToken = casToken.get();
        long timestamp = System.currentTimeMillis();
        long token = timestamp * 1000 + counterPerMill.getAndIncrement();
        while (!casToken.compareAndSet(lastToken, token)) {
            lastToken = casToken.get();
            long tmpTimestamp = System.currentTimeMillis();
            if (timestamp != tmpTimestamp) {
                counterPerMill.set(0);
            }
            timestamp = tmpTimestamp;
            token = timestamp * 1000 + counterPerMill.getAndIncrement();
        }
        counterPerMill.compareAndSet(999, 0);
        return String.valueOf(token);
    }

}
