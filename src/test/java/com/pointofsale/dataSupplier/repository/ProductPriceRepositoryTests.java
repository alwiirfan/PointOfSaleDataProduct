package com.pointofsale.dataSupplier.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.*;

import com.pointofsale.dataSupplier.entity.ProductStore;
import com.pointofsale.dataSupplier.entity.embeddable.ProductPrice;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductPriceRepositoryTests {
    
    @Autowired
    // private ProductPriceRepository productPriceRepository;

    @Test
    void productPriceRepository_findProductPriceActiveByProductStoreId_ReturnProductPrice() {
        // dummy product store
        String productStoreId = "121212";
        ProductStore dummyProductStore = new ProductStore();
        dummyProductStore.setId(productStoreId);

        // dummy product price
        ProductPrice.builder()
        .purchasePrice(new BigDecimal(100.00))
        .sellingPrice(new BigDecimal(200.00))
        // .productStore(dummyProductStore)
        .isActive(true)
        .build();

        // Act
        // Optional<ProductPrice> result = productPriceRepository.findFirstByIsActiveTrueAndProductStore_Id(dummyProductStore.getId());

        // Then
        // assertNotNull(result);
    }

    @Test
    void productPriceRepository_findProductPriceByProductStoreCode_ReturnProductPrice() {
        // dummy product store
        String productStoreCode = "121212";
        ProductStore dummyProductStore = new ProductStore();
        dummyProductStore.setProductCode(productStoreCode);

        // dummy product price
        ProductPrice.builder()
        .purchasePrice(new BigDecimal(100.00))
        .sellingPrice(new BigDecimal(200.00))
        // .productStore(dummyProductStore)
        // .isActive(true)
        .build();

        // Act
        // Optional<ProductPrice> result = productPriceRepository.findFirstByIsActiveTrueAndProductStore_ProductCode(dummyProductStore.getProductCode());

        // Then
        // assertNotNull(result);
    }

    @Test
    void productPriceRepository_findAllProductPriceByProductStoreCode_ReturnListProductPrice() {
        // dummy product store
        String productStoreCode = "121212";
        ProductStore dummyProductStore = new ProductStore();
        dummyProductStore.setProductCode(productStoreCode);

        // dummy product price
        ProductPrice.builder()
        .purchasePrice(new BigDecimal(100.00))
        .sellingPrice(new BigDecimal(200.00))
        // .productStore(dummyProductStore)
        .isActive(true)
        .build();

        // Act
        // List<ProductPrice> result = productPriceRepository.findAllByProductStore_ProductCode(dummyProductStore.getProductCode());

        // Then
        // assertNotNull(result);
    }

    @Test 
    void productPriceRepository_findAllProductPrice_ReturnPageProductPrice() {
        // dummy product price
        ProductPrice.builder()
        .purchasePrice(new BigDecimal(100.00))
        .sellingPrice(new BigDecimal(200.00))
        // .productStore(new ProductStore())
        .isActive(true)
        .build();

        // Act
        // List<ProductPrice> result = productPriceRepository.findAll(Specification.where(null), Pageable.unpaged()).getContent();

        // Then
        // assertNotNull(result);
    }
}
