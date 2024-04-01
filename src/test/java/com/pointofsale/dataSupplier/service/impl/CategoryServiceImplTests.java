package com.pointofsale.dataSupplier.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// import com.pointofsale.dataSupplier.constant.ECategory;
import com.pointofsale.dataSupplier.entity.Category;
import com.pointofsale.dataSupplier.repository.CategoryRepository;
import com.pointofsale.dataSupplier.service.CategoryService;

@SpringBootTest
public class CategoryServiceImplTests {
    
    // MOCK
    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    // private final CategoryService categoryService = new CategoryServiceImpl(categoryRepository);

    @Test
    void getCategoryOrSave_CategoryExists() {
        // Arrange
        // ECategory eCategory = ECategory.MAKANAN;
        // Category existingCategory = Category.builder().category(eCategory).build();

        // mock repos behavior
        // when(categoryRepository.findFirstByCategory(eCategory)).thenReturn(Optional.of(existingCategory));

        // Act
        // Category result = categoryService.getOrSave(eCategory);

        //Assert
        // verify(categoryRepository, times(1)).findFirstByCategory(eCategory);
        // verify(categoryRepository, never()).save(any());

        // assertEquals(existingCategory.getCategory(), eCategory);
        // assertEquals(existingCategory, result);
    }

    @Test
    void getCategoryOrSave_CategoryNotExists() {
        // Arrange
        // ECategory eCategory = ECategory.MINUMAN;
        // Category newCategory = Category.builder().category(eCategory).build();

        // mock repos behavior
        // when(categoryRepository.findFirstByCategory(eCategory)).thenReturn(Optional.empty());
        // when(categoryRepository.save(any())).thenReturn(newCategory);

        // Act
        // Category result = categoryService.getOrSave(eCategory);

        //Assert
        // verify(categoryRepository, times(1)).findFirstByCategory(eCategory);
        verify(categoryRepository, times(1)).save(any());

        // assertEquals(newCategory.getCategory(), eCategory);
        // assertEquals(newCategory, result);
    }
    
}
