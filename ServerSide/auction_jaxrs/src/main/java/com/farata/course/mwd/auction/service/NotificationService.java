package com.farata.course.mwd.auction.service;

import com.farata.course.mwd.auction.entity.BidDTO;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

/**
 * Created by prozorov on 30/09/14.
 */
@Singleton
public class NotificationService {

    @PostConstruct
    void init() {
        productToUserMap = new HashMap<>();
        subscriptions = new HashMap<>();
    }

    private static final Logger log = Logger.getLogger(NotificationService.class.getName());

    private Map<Integer, List<String>> productToUserMap;
    private Map<String, BiConsumer<Integer, BigDecimal>> subscriptions;

    public void subscribe(final String userId, final BiConsumer<Integer, BigDecimal> consumer) {
        subscriptions.computeIfAbsent(userId, key -> consumer);
        log.info("User " + userId + " subscribed to bid notifications");
    }

    public void unsubscribe(final String userId) {
        subscriptions.remove(userId);
        log.info("User " + userId + " unsubscribed from bid notifications");
    }

    public void notifySubscribers(int productId, BigDecimal price) {
        // Get all users who placed the bid.
        productToUserMap.getOrDefault(productId, Collections.<String>emptyList()).stream()
                // Find out if they subscribed to updates (WebSocket session still alive).
                .filter(subscriptions::containsKey)
                        // Notify them.
                .forEach(userId -> subscriptions.get(userId).accept(productId, price));
    }

    public void addUserToProduct(BidDTO bid)
    {
        productToUserMap.computeIfAbsent(bid.getProductId(), key -> new ArrayList<>()).add(String.valueOf(bid.getUserId()));
    }

}
