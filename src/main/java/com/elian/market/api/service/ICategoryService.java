package com.elian.market.api.service;

import com.elian.market.api.domain.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    Category update(Long id, Category category);
    void deleteById(Long id);
}
