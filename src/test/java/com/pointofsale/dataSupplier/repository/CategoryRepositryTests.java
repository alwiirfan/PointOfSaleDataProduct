package com.pointofsale.dataSupplier.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

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
        String categoryString = "pakaian";
        Category category = Category.builder()
        .category(categoryString.toUpperCase())
        .build();

        // Act
        Category resultCategory = categoryRepository.save(category);

        // Assert
        assertTrue(resultCategory.getId().length() > 0);
        assertNotNull(resultCategory);
        assertEquals(category, resultCategory);
    }
}
