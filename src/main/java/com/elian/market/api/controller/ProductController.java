package com.elian.market.api.controller;

import com.elian.market.api.domain.Product;
import com.elian.market.api.dto.ProductRequestDto;
import com.elian.market.api.dto.ProductResponseDto;
import com.elian.market.api.mapper.ProductMapper;
import com.elian.market.api.service.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImpl productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> findAll(Pageable pageable) {

        Page<ProductResponseDto> products = productService
                .findAll(pageable)
                .map(productMapper::toResponseDto);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable Long id) {

        Product product = productService.findById(id);

        return ResponseEntity.ok(
                productMapper.toResponseDto(product)
        );
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponseDto>> findByCategory(
            @PathVariable Long categoryId,
            Pageable pageable) {

        Page<ProductResponseDto> products = productService
                .findByCategory(categoryId, pageable)
                .map(productMapper::toResponseDto);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDto>> search(
            @RequestParam String name,
            Pageable pageable) {

        Page<ProductResponseDto> products = productService
                .search(name, pageable)
                .map(productMapper::toResponseDto);

        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(
            @Valid @RequestBody ProductRequestDto dto) {

        Product product = productMapper.toEntity(dto);

        Product savedProduct = productService.create(
                product,
                dto.getCategoryId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productMapper.toResponseDto(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto dto) {

        Product product = productMapper.toEntity(dto);

        Product updatedProduct = productService.update(
                id,
                product,
                dto.getCategoryId()
        );

        return ResponseEntity.ok(
                productMapper.toResponseDto(updatedProduct)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id) {

        productService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
