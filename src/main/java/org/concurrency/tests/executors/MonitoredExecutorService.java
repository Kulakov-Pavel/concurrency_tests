package org.concurrency.tests.executors;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MonitoredExecutorService extends ThreadPoolExecutor {
    public static Map<String, Timer> map = new ConcurrentHashMap<>();
    public MonitoredExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public void execute(Runnable command) {
        map.put(getHash(command), new Timer(Timestamp.valueOf(LocalDateTime.now())));
        System.out.println("execute " + command + " in thread " + Thread.currentThread().getName());
        super.execute(command);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        map.get(getHash(r)).setBefore(Timestamp.valueOf(LocalDateTime.now()));
        System.out.println("beforeExecute " + r + " in thread " + t.getName());
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        map.get(getHash(r)).setAfter(Timestamp.valueOf(LocalDateTime.now()));
        System.out.println("afterExecute " + r + " in thread " + Thread.currentThread().getName() + "\n");
        super.afterExecute(r, t);
    }

    private String getHash(Runnable r) {
        return r.toString().subSequence(31, 40).toString();
    }

    @Setter
    @Getter
    public static class Timer {
        public Timestamp execute;
        public Timestamp before;
        public Timestamp after;

        public Timer(Timestamp execute) {
            this.execute = execute;
        }
    }
}
