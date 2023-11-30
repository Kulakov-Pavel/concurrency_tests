package org.concurrency.tests.synchron;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SupplierConsumerRRLock {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final Condition readCondition = readLock.newCondition();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private final Condition writeCondition = writeLock.newCondition();

    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity;

    public SupplierConsumerRRLock(int capacity) {
        this.capacity = capacity;
    }

    public void add(int value) {
        writeLock.lock();
        try {
            while (queue.size() >= capacity) {
                await(writeCondition);
            }
            System.out.printf("""
                    add(%d)  size: %d
                    """, value, queue.size());
            queue.offer(value);
        } finally {
            readCondition.signalAll();
            writeLock.unlock();
        }
    }

    public int take() {
        readLock.lock();
        try {
            while (queue.isEmpty()) {
                await(readCondition);
            }
            var value = queue.poll();
            System.out.printf("""
                    take(%d)  size: %d
                    """, value, queue.size());
            return value;
        } finally {
            writeCondition.signalAll();
            readLock.unlock();
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
