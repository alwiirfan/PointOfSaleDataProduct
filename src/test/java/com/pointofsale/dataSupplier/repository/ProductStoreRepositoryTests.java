package com.pointofsale.dataSupplier.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.*;

// import com.pointofsale.dataSupplier.constant.ECategory;
import com.pointofsale.dataSupplier.entity.Category;
import com.pointofsale.dataSupplier.entity.ProductStore;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductStoreRepositoryTests {
    
    @Autowired
    private ProductStoreRepository productStoreRepository;

    @Test
    void productStoreRepository_findFirstByProductCode_ReturnProductStore() {
        // dummy product store
        String productCode = "121212";
        ProductStore dummyProductStore = new ProductStore();
        dummyProductStore.setProductCode(productCode);
        dummyProductStore.setProductName("dummy product");
        dummyProductStore.setDescription("dummy product");
        // dummyProductStore.setProductPrices(null);
        // dummyProductStore.setCategory(new Category(ECategory.MAKANAN));
        dummyProductStore.setMerk("ALL");

        // Act
        Optional<ProductStore> result = productStoreRepository.findFirstByProductCode(dummyProductStore.getProductCode());

        // Then
        assertNotNull(result);
    }
}
