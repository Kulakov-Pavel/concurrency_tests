package org.concurrency.tests.futures;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                    System.out.println("first");
                    return 1;
                })
                .thenCompose(v -> CompletableFuture.supplyAsync(() -> {
                            System.out.println("second");
                            return v * 2;
                        })
                        .thenCompose(v1 -> CompletableFuture.supplyAsync(() -> {
                            System.out.println("third");
                            return v1 * 4;
                        }))
                        .thenApply(v2 -> {
                            System.out.println("fourth");
                            return v2 * 2;
                        })
                ).thenApply(v -> v*2);


        System.out.println(future.join());

        SECONDS.sleep(1);

    }
}
