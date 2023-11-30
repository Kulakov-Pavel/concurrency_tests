package org.concurrency.tests;

import org.concurrency.tests.synchron.SupplierConsumerRRLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(16);
        SupplierConsumerRRLock queue = new SupplierConsumerRRLock(10);

        for (int i = 0; i < 100; i++) {
            int value = i;
            executor.execute(() -> queue.add(value));
            executor.execute(queue::take);
        }

        executor.shutdown();
    }

}