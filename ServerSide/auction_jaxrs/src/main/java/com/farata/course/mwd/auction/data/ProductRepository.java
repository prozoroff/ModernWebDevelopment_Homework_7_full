package com.farata.course.mwd.auction.data;

import com.farata.course.mwd.auction.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by prozorov on 18/09/14.
 */
public class ProductRepository {

    private List<Product> productsList;

    public ProductRepository()
    {
        initProducts();
    }

    private void initProducts() {
        productsList = new ArrayList<Product>() {

            public boolean add(Product dto) {
                return !((dto == null) || (dto.getId() == null)) && super.add(dto);
            }

            public Product remove(int index) {
                Product dto = super.remove(index);
                return dto;
            }
        };

        Random random = new Random(LocalDateTime.now().getHour());
        for (int i = 1; i <= 6; i++) {
            productsList.add(new Product(i, "Item " + i, "0" + i + ".jpg",
                    "",
                    "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore adipiscing elit. Ut enim.",
                    2,
                    LocalDateTime.now().plusDays(random.nextInt(10)),
                    new BigDecimal(12),
                    new BigDecimal(35),
                    "123", 5
            ));
        }
    }

    public List<Product> findAllProducts() {
        return Collections.unmodifiableList(productsList);
    }

    public List<Product> findAllFeaturedProducts() {
        // TODO featured products
        return Collections.unmodifiableList(productsList);
    }

    public Product findProductById(int id) {
        Product result = null;
        for (Product product : productsList) {
            if (product.getId().compareTo(id) == 0) {
                result = product;
            }
        }
        return result;
    }

    public void updateProduct(Product product)
    {
        Product productToUpdate = productsList.stream()
                .filter(p->p.getId() == product.getId()).collect(Collectors.toList()).get(0);

        int index = productsList.lastIndexOf(productToUpdate);
        productsList.set(index, product);
    }

}
