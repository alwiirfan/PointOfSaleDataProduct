package com.pointofsale.dataSupplier.service.impl;

import com.pointofsale.dataSupplier.constant.ECategory;
import com.pointofsale.dataSupplier.entity.Category;
import com.pointofsale.dataSupplier.repository.CategoryRepository;
import com.pointofsale.dataSupplier.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category getOrSave(ECategory eCategory) {
        Category category = Category.builder()
                .category(eCategory)
                .build();
        return categoryRepository
                .findFirstByCategory(eCategory)
                .orElseGet(() -> categoryRepository.save(category));
    }
}
