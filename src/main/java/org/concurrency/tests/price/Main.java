package org.concurrency.tests.price;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        ThreadLocal<Random> random  = ThreadLocal.withInitial(Random::new);

        Thread a = new Thread(() -> {
            Random random1 = random.get();
            int i = random.get().nextInt();
            int j = random.get().nextInt();
            System.out.println(i + " " + j);
            System.out.println(random1 + "  a");
        });

        Thread b = new Thread(() -> {
            Random random1 = random.get();
            int i = random.get().nextInt();
            int j = random.get().nextInt();
            System.out.println(i + " " + j);
            System.out.println(random1 + "  b");
        });

        a.start();
        b.start();

    }
}
