package org.concurrency.tests.price;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PriceAggregator {

    private PriceRetriever priceRetriever = new PriceRetriever();

    public void setPriceRetriever(PriceRetriever priceRetriever) {
        this.priceRetriever = priceRetriever;
    }

    private Collection<Long> shopIds = Set.of(10l, 45l, 66l, 345l, 234l, 333l, 67l, 123l, 768l);

    public void setShops(Collection<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public double getMinPrice(long itemId) {
        // place for your code
        List<Long> shops = new ArrayList<>(shopIds);
        List<CompletableFuture<Integer>> futures = shops.stream()
                .map(id -> new CompletableFuture<Integer>())
                .collect(Collectors.toList());
        for (int i = 0; i < shops.size(); i++) {
            int idx = i;
            futures.get(i).thenApplyAsync(item -> priceRetriever.getPrice(itemId, shops.get(idx)));
        }
        CompletableFuture<Void> future = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        future.orTimeout(3, TimeUnit.SECONDS);
        return futures.stream()
                .filter(CompletableFuture::isDone)
                .map(CompletableFuture::join)
                .min(Double::compare)
                .orElse(null);
    }
}
