package com.pointofsale.dataSupplier.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import com.pointofsale.dataSupplier.constant.ECategory;
import com.pointofsale.dataSupplier.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CategoryRepositryTests {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Test
    void CategoryRepository_SaveCategory_ReturSaveCategory() {
        // Arrange
        Category category = Category.builder()
        // .category(ECategory.MAKANAN)
        .build();

        // Act
        // Category resultCategory = categoryRepository.findFirstByCategory(ECategory.MINUMAN).orElseGet(() -> categoryRepository.save(category)); 

        // Then
        // Optional<Category> categoryById = categoryRepository.findById(resultCategory.getId());

        // // Assert
        // assertTrue(categoryById.isPresent());
        // assertNotNull(resultCategory);
        // assertEquals(category, resultCategory);
    }
}
