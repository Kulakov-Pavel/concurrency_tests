package org.concurrency.tests.synchron;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SupplierConsumerLock {

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity;

    public SupplierConsumerLock(int capacity) {
        this.capacity = capacity;
    }

    public void add(int value) {
        lock.lock();
        try {
            while (queue.size() >= capacity) {
                await(condition);
            }
            System.out.printf("""
                    add(%d)  size: %d
                    """, value, queue.size());
            queue.offer(value);
        } finally {
            condition.signalAll();
            lock.unlock();
        }
    }

    public int take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                await(condition);
            }
            var value = queue.poll();
            System.out.printf("""
                    take(%d)  size: %d
                    """, value, queue.size());
            return value;
        } finally {
            condition.signalAll();
            lock.unlock();
        }
    }

    private void await(Condition condition) {
        try {
            condition.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
