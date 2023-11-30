package org.concurrency.tests.price;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Semaphore {

    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(5);
        Queue<String> queue = new LinkedList<>();
        boolean empty = true;

        IntStream.rangeClosed(1, 10)
                .forEach(i -> {
                            if (i <= 5) {
                                new Supplier(semaphore, queue).start();
                            } else {
                                new Consumer(semaphore, queue).start();
                            }
                        }
                );


    }

    static class Supplier extends Thread {

        java.util.concurrent.Semaphore semaphore;
        Queue<String> queue;

        Supplier(java.util.concurrent.Semaphore semaphore, Queue<String> queue) {
            this.semaphore = semaphore;
            this.queue = queue;
        }

        @Override
        public void run() {
            for (; ; ) {
                try {
                    semaphore.acquire();
                    if (queue.size() < 10) {
                        String item = "Item #" + counter.getAndIncrement();
                        queue.offer(item);
                        System.out.println(item + " has added to queue by " + Thread.currentThread().getName() + " Size: " + queue.size());
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    semaphore.release();
                    asleep(4);
                }
            }
        }
    }

    static class Consumer extends Thread {

        java.util.concurrent.Semaphore semaphore;
        Queue<String> queue;

        Consumer(java.util.concurrent.Semaphore semaphore, Queue<String> queue) {
            this.semaphore = semaphore;
            this.queue = queue;
        }

        @Override
        public void run() {
            for (; ; ) {
                try {
                    semaphore.acquire();
                    if (!queue.isEmpty()) {
                        String item = queue.poll();
                        System.out.println(item + " has removed from queue by " + Thread.currentThread().getName() + " Size: " + queue.size());
                        SECONDS.sleep(1);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    semaphore.release();
                    asleep(3);
                }
            }
        }
    }

    static void asleep(int sec) {
        try {
            SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
