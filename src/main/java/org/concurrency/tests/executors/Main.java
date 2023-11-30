package org.concurrency.tests.executors;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        ExecutorService pool = new MonitoredExecutorService(
                2, 2, 60, SECONDS, new LinkedBlockingQueue<>());

        Callable<String> r1 = () -> "Runnable + #" + 1;
        Callable<String> r2 = () -> "Runnable + #" + 2;
        Callable<String> r3 = () -> "Runnable + #" + 3;
        Callable<String> r4 = () -> "Runnable + #" + 4;
        Callable<String> r5 = () -> "Runnable + #" + 5;

        pool.invokeAll(List.of(r1, r2, r3, r4, r5));

        pool.shutdown();
    }

    private static void asleep(int value) throws InterruptedException {
        SECONDS.sleep(value);
    }
}
