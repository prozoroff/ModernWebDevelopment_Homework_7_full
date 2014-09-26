package com.farata.course.mwd.auction.data;

import com.farata.course.mwd.auction.entity.Bid;
import com.farata.course.mwd.auction.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by prozorov on 18/09/14.
 */
public class BidRepository {

    private List<Bid> bids = new ArrayList<Bid>();
    private final StampedLock sl = new StampedLock();

    ProductRepository productRepository;
    UserRepository userRepository;

    public BidRepository(ProductRepository productRepository, UserRepository userRepository)
    {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<Bid> getBids(Predicate<Bid> predicate)
    {
        return bids.stream().filter(predicate).collect(Collectors.toList());
    }

    public Bid getBid(int id)
    {
        List<Bid> desiredBids = getBids(b -> b.getId() == id);
        return (desiredBids == null || desiredBids.size()==0)?null:desiredBids.get(0);
    }

    public List<Bid> getBids()
    {
        return bids;
    }

    public String placeBid(Integer productId, BigDecimal amount, Integer desiredQuantity, int userId)
    {
        long stamp = sl.writeLock();

        try {
            int id = bids.size();
            LocalDateTime now = LocalDateTime.now();

            List<Bid> productBids = bids.stream().filter(b -> b.getProduct().getId() == productId)
                    .sorted(Comparator.comparing(b -> b.getAmount())).collect(Collectors.toList());

            Product product = productRepository.findProductById(productId);

            if (productBids.size() > 0 && (productBids.get(productBids.size() - 1).getAmount().compareTo(amount) == 1)) {
                return "[{\"message\": \"Sorry, there is at least one amount that larger than your: " +
                        productBids.get(productBids.size() - 1).getAmount() + " by user "
                        + productBids.get(productBids.size() - 1).getUser().getName() + "\", \"status\": \"cancel\"}]";
            }

            if ( product.getMinPrice().compareTo(amount) == 1) {
                return "[{\"message\": \"Sorry, your bid amount must be greater than minimal price: " +
                        product.getMinPrice() + "\", \"status\": \"cancel\"}]";
            }

            if (productRepository.findProductById(productId).getQuantity() < desiredQuantity) {
                return "[{\"message\": \"Not enough products: " + product.getQuantity() + "\", \"status\": \"cancel\"}]";
            }

            CleanUpBids(id);

            bids.add(new Bid(getBids().size(), productRepository.findProductById(productId), amount, desiredQuantity,
                    userRepository.getUserById(userId), LocalDateTime.now(), true));

            UpdateReservedPrice(productId, amount);

            return "[{\"message\": \"Good job! Your bid is winning now :)\", \"status\": \"renew\"}]";
        }
        finally {
            sl.unlockWrite(stamp);
        }
    }

    private void UpdateReservedPrice(int id, BigDecimal amount)
    {
        Product productToUpdate = productRepository.findAllProducts().stream().
                filter(p->p.getId() == id).collect(Collectors.toList()).get(0);
        productToUpdate.setReservedPrice(amount);

        productRepository.updateProduct(productToUpdate);
    }

    private void CleanUpBids(int productId)
    {
        for(Iterator<Bid> allBids = bids.stream().filter(b->b.getProduct().getId() == productId).iterator(); allBids.hasNext(); ) {
            allBids.next().setWinning(false);
        }
    }

}
