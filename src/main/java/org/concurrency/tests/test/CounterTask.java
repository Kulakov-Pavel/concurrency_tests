package org.concurrency.tests.test;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class CounterTask extends RecursiveTask<Integer> {

    private final int threshold;
    private final int searchValue;
    private final List<String> list;
    private final int left;
    private final int right;

    public CounterTask(List<String> list, int left, int right, int searchValue, int threshold) {
        this.list = list;
        this.left = left;
        this.right = right;
        this.searchValue = searchValue;
        this.threshold = threshold;
    }

    @Override
    protected Integer compute() {

        if(right - left <= threshold) {
            return count();
        } else {
            int median = (right - left) / 2;
            CounterTask a = new CounterTask(list, left, median, searchValue, threshold);
            CounterTask b = new CounterTask(list, median, right, searchValue, threshold);
            invokeAll(a, b);
            return a.join() + b.join();
        }
    }

    private int count() {
        int counter = 0;
        for (int i = left; i < right; i++) {
            if(Integer.valueOf(list.get(i)).equals(searchValue))
                ++counter;
        }
        return counter;
    }
}
