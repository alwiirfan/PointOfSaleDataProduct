package com.pointofsale.dataSupplier.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.math.BigDecimal;

import com.pointofsale.dataSupplier.constant.ResponseMessage;
import com.pointofsale.dataSupplier.dto.request.SearchProductStoreRequest;
import com.pointofsale.dataSupplier.entity.*;
import com.pointofsale.dataSupplier.entity.embeddable.ProductPrice;

@SpringBootTest
public class ProductPriceServiceImplTest {
    
    // private final ProductPriceRepository productPriceRepository = mock(ProductPriceRepository.class);
    // private final ProductPriceService productPriceService = new ProductPriceServiceImpl(productPriceRepository);

    @Test
    void itShouldWhenReturnSuccessGetAllActiveProductPriceByProductStoreCode() {
        // dummy product store
        String productCode = "121212";
        ProductStore dummyProductStore = new ProductStore();
        dummyProductStore.setProductCode(productCode);

        // list of product prices
        List<ProductPrice> dummyProductPrices = new ArrayList<>();
        // dummyProductPrices.add(new ProductPrice(new BigDecimal(1000.00), new BigDecimal(2000.00), 20, dummyProductStore, true));
        // dummyProductPrices.add(new ProductPrice(new BigDecimal(2000.00), new BigDecimal(3000.00), 30, dummyProductStore, true));
        // dummyProductPrices.add(new ProductPrice(new BigDecimal(3000.00), new BigDecimal(4000.00), 40, dummyProductStore, true));

        // mock behavior
        // when(productPriceRepository.findAllByProductStore_ProductCode(productCode)).thenReturn(dummyProductPrices);

        // Act
        // List<ProductPrice> result = productPriceService.getAllProductPriceByProductStoreCode(productCode);

        // Verify
        // verify(productPriceRepository, times(1)).findAllByProductStore_ProductCode(productCode);

        // // Assert result size
        // assert(result.size() == 3);

        // for (int i = 0; i < dummyProductPrices.size(); i++) {
        //     assertEquals(dummyProductPrices.get(i), result.get(i));
        //     assertEquals(dummyProductPrices.get(i).getPurchasePrice(), result.get(i).getPurchasePrice());
        //     assertEquals(dummyProductPrices.get(i).getSellingPrice(), result.get(i).getSellingPrice());
        //     assertEquals(dummyProductPrices.get(i).getProductStore(), result.get(i).getProductStore());
        //     assertEquals(dummyProductPrices.get(i).getStock(), result.get(i).getStock());
        //     assertEquals(dummyProductPrices.get(i).getIsActive(), result.get(i).getIsActive());
        }

    // }

    @Test
    void itShouldWhenReturnSuccessGetActiveProductPriceByProductStoreId() {
        // dummy product store
        // String productStoreId = "121212";
        // ProductStore dummyProductStore = new ProductStore();
        // dummyProductStore.setId(productStoreId);

        // dummy product price
        // ProductPrice dummyProductPrice = new ProductPrice();
        // dummyProductPrice.setPurchasePrice(new BigDecimal(100.00));
        // dummyProductPrice.setSellingPrice(new BigDecimal(200.00));
        // dummyProductPrice.setProductStore(dummyProductStore);
        // dummyProductPrice.setIsActive(true);

        // // mock behavior
        // when(productPriceRepository.findFirstByIsActiveTrueAndProductStore_Id(productStoreId)).thenReturn(Optional.of(dummyProductPrice));

        // // Act
        // ProductPrice result = productPriceService.getActivePriceByProductStoreId(productStoreId);

        // // verify
        // verify(productPriceRepository, times(1)).findFirstByIsActiveTrueAndProductStore_Id(productStoreId);

        // assert
    //     assertEquals(dummyProductPrice, result);
    //     assertEquals(dummyProductPrice.getPurchasePrice(), result.getPurchasePrice());
    //     assertEquals(dummyProductPrice.getSellingPrice(), result.getSellingPrice());
    //     assertEquals(dummyProductPrice.getStock(), result.getStock());
    //     assertEquals(dummyProductPrice.getIsActive(), result.getIsActive());
    }

    @Test
    void itShouldWhenReturnExceptionNotFoundGetActiveProductPriceByProductStoreId() {
        // dummy product
        String productStoreId = "121212";
        ProductStore dummyProductStore = new ProductStore();
        dummyProductStore.setId(productStoreId);

        // dummy product price
        ProductPrice dummyProductPrice = new ProductPrice();
        dummyProductPrice.setPurchasePrice(new BigDecimal(100.00));
        dummyProductPrice.setSellingPrice(new BigDecimal(200.00));
        // dummyProductPrice.setProductStore(dummyProductStore);
        dummyProductPrice.setIsActive(true);

        // mock behavior
        // when(productPriceRepository.findFirstByIsActiveTrueAndProductStore_Id(productStoreId)).thenReturn(Optional.empty());

        // Error
        // ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productPriceService.getActivePriceByProductStoreId(productStoreId));

        // verify
        // verify(productPriceRepository, times(1)).findFirstByIsActiveTrueAndProductStore_Id(productStoreId);

        // // assert
        // assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        // assertEquals(ResponseMessage.getNotFoundResource(ProductPrice.class), exception.getReason());
        
    }

    @Test
    void itShouldWhenReturnSuccessGetProductPriceById() {
        // dummy product price
        String productPriceId = "121212";
        ProductPrice dummyProductPrice = new ProductPrice();
        // dummyProductPrice.setId(productPriceId);

        // mock behavior
        // when(productPriceRepository.findById(productPriceId)).thenReturn(Optional.of(dummyProductPrice));

        // Act
        // ProductPrice result = productPriceService.getProductPriceById(productPriceId);

        // verify
        // verify(productPriceRepository, times(1)).findById(productPriceId);

        // // assert
        // assertEquals(dummyProductPrice, result);
        // assertEquals(dummyProductPrice.getId(), result.getId());
    }

    @Test
    void itShouldWhenReturnExceptionNotFoundGetProductPriceById() {
        // dummy product price
        String productPriceId = "121212";
        ProductPrice dummyProductPrice = new ProductPrice();
        // dummyProductPrice.setId(productPriceId);

        // // mock behavior
        // when(productPriceRepository.findById(productPriceId)).thenReturn(Optional.empty());

        // // Error
        // ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productPriceService.getProductPriceById(productPriceId));

        // // verify
        // verify(productPriceRepository, times(1)).findById(productPriceId);

        // // assert
        // assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        // assertEquals(ResponseMessage.getNotFoundResource(ProductPrice.class), exception.getReason());

    }

    @Test
    void itShouldWhenReturnSuccessGetActiveProductPriceByProductStoreCode() {
        // dummy product store
        String productStoreCode = "121212";
        ProductStore dummyProductStore = new ProductStore();
        dummyProductStore.setProductCode(productStoreCode);

        // dummy product price
        ProductPrice dummyProductPrice = new ProductPrice();
        dummyProductPrice.setPurchasePrice(new BigDecimal(100.00));
        dummyProductPrice.setSellingPrice(new BigDecimal(200.00));
        // dummyProductPrice.setProductStore(dummyProductStore);
        dummyProductPrice.setIsActive(true);

        // mock behavior
        // when(productPriceRepository.findFirstByIsActiveTrueAndProductStore_ProductCode(productStoreCode)).thenReturn(Optional.of(dummyProductPrice));

        // // Act
        // ProductPrice result = productPriceService.getActivePriceByProductStoreCode(productStoreCode);

        // // verify
        // verify(productPriceRepository, times(1)).findFirstByIsActiveTrueAndProductStore_ProductCode(productStoreCode);

        // // assert 
        // assertEquals(dummyProductPrice, result);
        // assertEquals(dummyProductPrice.getPurchasePrice(), result.getPurchasePrice());
        // assertEquals(dummyProductPrice.getSellingPrice(), result.getSellingPrice());
        // assertEquals(dummyProductPrice.getStock(), result.getStock());
        // assertEquals(dummyProductPrice.getIsActive(), result.getIsActive());
    }

    @Test
    void itShouldWhenReturnExceptionNotFoundGetActiveProductPriceByProductStoreCode() {
        // dummy product store
        String productStoreCode = "121212";
        ProductStore dummyProductStore = new ProductStore();
        dummyProductStore.setProductCode(productStoreCode);

        // dummy product price
        ProductPrice dummyProductPrice = new ProductPrice();
        dummyProductPrice.setPurchasePrice(new BigDecimal(100.00));
        dummyProductPrice.setSellingPrice(new BigDecimal(200.00));
        // dummyProductPrice.setProductStore(dummyProductStore);
        // dummyProductPrice.setIsActive(true);

        // // mock behavior
        // when(productPriceRepository.findFirstByIsActiveTrueAndProductStore_ProductCode(productStoreCode)).thenReturn(Optional.empty());

        // // Error
        // ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productPriceService.getActivePriceByProductStoreCode(productStoreCode));

        // // verify
        // verify(productPriceRepository, times(1)).findFirstByIsActiveTrueAndProductStore_ProductCode(productStoreCode);

        // assert 
        // assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        // assertEquals(ResponseMessage.getNotFoundResource(ProductPrice.class), exception.getReason());
    }

    @SuppressWarnings("unchecked")
    @Test
    void itShouldWhenReturnSuccessGetAllActiveProductPrice() {
        // pageable
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        // mock request
        SearchProductStoreRequest request = new SearchProductStoreRequest();
        request.setProductCode("123");
        request.setProductName("ProductABC");
        request.setMinSellingPrice(50);
        request.setMaxSellingPrice(100);
        request.setPurchasePrice(30);

        // Mock data
        List<ProductPrice> mockProductPrices = new ArrayList<>();
        // mockProductPrices.add(new ProductPrice(new BigDecimal("80.0"), new BigDecimal("100.0"), 10, null, true));
        // mockProductPrices.add(new ProductPrice(new BigDecimal("30.0"), new BigDecimal("90.0"), 15, null, true));

        // Page<ProductPrice> productPricePage = new PageImpl<>(mockProductPrices, pageable, mockProductPrices.size());

        // // mock behavior
        // when(productPriceRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPricePage);

        // // Act
        // Page<ProductPrice> result = productPriceService.getAllActiveProductPrice(request, page, size);
    
        // // verify
        // verify(productPriceRepository, times(1)).findAll(any(Specification.class), eq(pageable));

        // // assert
        // for (int i = 0; i < mockProductPrices.size(); i++) {
        //     assertEquals(mockProductPrices.get(i).getId(), result.getContent().get(i).getId());
        //     assertEquals(mockProductPrices.get(i).getPurchasePrice(), result.getContent().get(i).getPurchasePrice());
        //     assertEquals(mockProductPrices.get(i).getSellingPrice(), result.getContent().get(i).getSellingPrice());
        //     assertEquals(mockProductPrices.get(i).getProductStore(), result.getContent().get(i).getProductStore());
        //     assertEquals(mockProductPrices.get(i).getStock(), result.getContent().get(i).getStock());
        // }

        // assertEquals(productPricePage.getTotalElements(), result.getTotalElements());
        // assertEquals(mockProductPrices.size(), result.getContent().size());
        // assertEquals(mockProductPrices.get(0), result.getContent().get(0));
        // assertEquals(mockProductPrices.get(1), result.getContent().get(1));
    }
}