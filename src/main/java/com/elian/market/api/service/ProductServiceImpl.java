package com.elian.market.api.service;

import com.elian.market.api.domain.Category;
import com.elian.market.api.domain.Product;
import com.elian.market.api.exception.ResourceNotFoundException;
import com.elian.market.api.repository.CategoryRepository;
import com.elian.market.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findByCategory(Long categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException(
                    "Categoria no encontrada con id: " + categoryId
            );
        }
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> search(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(
                name,
                pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Producto no encontrado con id: " + id
                        )
                );
    }

    @Override
    @Transactional
    public Product create(Product product, Long categoryId) {
        validateProduct(product);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Categoria no encontrada con id: " + categoryId
                        )
                );

        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Long id, Product product, Long categoryId) {
        validateProduct(product);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Producto no encontrado con id: " + id
                        )
                );

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Categoria no encontrada con id: " + categoryId
                        )
                );

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setCategory(category);

        return productRepository.save(existingProduct);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Producto no encontrado con id: " + id
            );
        }

        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void decreaseStock(Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor a 0"
            );
        }

        Product product = findById(productId);

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException(
                    "Stock insuficiente para el producto: " + product.getName()
            );
        }

        product.setStock(product.getStock() - quantity);

        productRepository.save(product);
    }

    private void validateProduct(Product product) {

        if (product.getPrice() == null ||
                product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "El precio no puede ser negativo"
            );
        }
        if (product.getStock() == null || product.getStock() < 0) {

            throw new IllegalArgumentException(
                    "El stock no puede ser negativo"
            );
        }
    }
}
