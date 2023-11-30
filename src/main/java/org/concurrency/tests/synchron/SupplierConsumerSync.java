package org.concurrency.tests.synchron;

import java.util.LinkedList;
import java.util.Queue;

import static java.util.concurrent.TimeUnit.SECONDS;

public class SupplierConsumerSync {

    private final Queue<Integer> queue = new LinkedList<>();
    private final Object lock = new Object();

    public void add(int value) {
        synchronized (lock) {
            while (queue.size() >= 5) {
                await(lock);
            }
            queue.offer(value);
            System.out.printf("""
                    add(%d)  size: %d
                    """, value, queue.size());
            lock.notifyAll();
        }
    }

    public int take() {
        synchronized (lock) {
            while (queue.isEmpty()) {
                await(lock);
            }
            asleep(1);
            lock.notifyAll();
            var value = queue.poll();
            System.out.printf("""
                    take(%d)  size: %d
                    """, value, queue.size());
            return value;
        }
    }

    private void await(Object lock) {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void asleep(int value) {
        try {
            SECONDS.sleep(value);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
