package com.elian.market.api.controller;

import com.elian.market.api.domain.Category;
import com.elian.market.api.dto.CategoryRequestDto;
import com.elian.market.api.dto.CategoryResponseDto;
import com.elian.market.api.mapper.CategoryMapper;
import com.elian.market.api.repository.CategoryRepository;
import com.elian.market.api.service.CategoryServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAll() {
        List<CategoryResponseDto> categories = categoryService
                .findAll()
                .stream()
                .map(categoryMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok(categoryMapper.toResponseDto(category));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> save(@Valid @RequestBody CategoryRequestDto dto) {
        Category category = categoryMapper.toEntity(dto);
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toResponseDto(savedCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto dto) {
        Category category = categoryMapper.toEntity(dto);
        Category updatedCategory = categoryService.update(id, category);
        return ResponseEntity.ok(categoryMapper.toResponseDto(updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
