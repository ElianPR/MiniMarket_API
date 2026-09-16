package com.elian.market.api.service;

import com.elian.market.api.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    Page<Product> findAll(Pageable pageable);

    Page<Product> findByCategory(Long categoryId, Pageable pageable);

    Page<Product> search(String name, Pageable pageable);

    Product findById(Long id);

    Product create(Product product, Long categoryId);

    Product update(Long id, Product product, Long categoryId);

    void deleteById(Long id);

    void decreaseStock(Long productId, Integer quantity);
}
