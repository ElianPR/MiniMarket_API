package com.elian.market.api.service;

import com.elian.market.api.domain.Category;
import com.elian.market.api.exception.ResourceNotFoundException;
import com.elian.market.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Categoria no encontrada con el id: " + id)
        );
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(Long id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException( "Categoria no encontrada con id: " + id ) );
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria no encontrada con id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
