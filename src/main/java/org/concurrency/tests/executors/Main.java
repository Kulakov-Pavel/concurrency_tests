package org.concurrency.tests.executors;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(4);

        Callable<String> c1 = () -> {
            asleep(5);
            System.out.println(Thread.currentThread().getName());
            return "Callable #1";
        };
        Callable<String> c2 = () -> {
            asleep(5);
            System.out.println(Thread.currentThread().getName());
            return "Callable #2";
        };

        pool.invokeAll(List.of(c1, c2));

        pool.shutdown();
        boolean termination = pool.awaitTermination(2, SECONDS);
        System.out.println(termination);
    }

    private static void asleep(int value) throws InterruptedException {
        SECONDS.sleep(value);
    }
}
