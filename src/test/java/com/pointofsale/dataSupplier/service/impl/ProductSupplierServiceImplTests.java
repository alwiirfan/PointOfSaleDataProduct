package com.pointofsale.dataSupplier.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.pointofsale.dataSupplier.constant.ResponseMessage;
import com.pointofsale.dataSupplier.dto.request.NewProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.response.ProductSupplierResponse;
import com.pointofsale.dataSupplier.entity.ProductSupplier;
import com.pointofsale.dataSupplier.repository.ProductSupplierRepository;
import com.pointofsale.dataSupplier.service.ProductSupplierService;
import com.pointofsale.dataSupplier.util.ValidationUtil;

@SpringBootTest
public class ProductSupplierServiceImplTests {
    
    private final ProductSupplierRepository productSupplierRepository = mock(ProductSupplierRepository.class);
    private final ValidationUtil validationUtil = mock(ValidationUtil.class);
    private final ProductSupplierService productSupplierService = new ProductSupplierServiceImpl(productSupplierRepository, validationUtil);

    @Test
    void itShouldReturnSuccessWhenCreateProductSupplier() {
        // dummy new product request
        NewProductSupplierRequest dummyRequest = new NewProductSupplierRequest();
        dummyRequest.setProductName("dummy product");
        dummyRequest.setUnitPrice(new BigDecimal(100.00));
        dummyRequest.setTotalItem(20);

        long dummyTotalItem = dummyRequest.getTotalItem();
        BigDecimal dummyUnitPrice = dummyRequest.getUnitPrice();
        BigDecimal dummyTotalPrice = dummyUnitPrice.multiply(BigDecimal.valueOf(dummyTotalItem));
        
        // dummy product store
        ProductSupplier dummySupplier = ProductSupplier
                            .builder()
                            .productName(dummyRequest.getProductName())
                            .unitPrice(dummyRequest.getUnitPrice())
                            .totalItem(dummyRequest.getTotalItem())
                            .totalPrice(dummyTotalPrice)
                            .build();

        // when
        when(productSupplierRepository.save(dummySupplier)).thenReturn(dummySupplier);

        // act
        ProductSupplierResponse result = productSupplierService.create(dummyRequest);

        // verify
        verify(productSupplierRepository, times(1)).save(dummySupplier);
        verify(validationUtil, times(1)).validate(dummyRequest);

        // assert
        assertNotNull(result);

        assertEquals(dummySupplier.getId(), result.getProductSupplierId());
        assertEquals(dummySupplier.getProductName(), result.getProductName());
        assertEquals(dummySupplier.getUnitPrice(), result.getUnitPrice());
        assertEquals(dummySupplier.getTotalItem(), result.getTotalItem());
        assertEquals(dummySupplier.getTotalPrice(), result.getTotalPrice());
    
    }

    @Test
    void itShouldReturnSuccessWhenGetAllProductsStore() {
        // dummy page
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        // dummy request
        SearchProductSupplierRequest dummyRequest = new SearchProductSupplierRequest();
        dummyRequest.setProductName("dummy product");
        dummyRequest.setTotalItem(20);
        dummyRequest.setMaxTotalPrice(2000);
        dummyRequest.setMaxUnitPrice(200);
        dummyRequest.setMinUnitPrice(100);
        dummyRequest.setMinTotalPrice(1000);

        // dummy product store
        List<ProductSupplier> productSuppliers = new ArrayList<>();
        productSuppliers.add(new ProductSupplier("dummy product1", new BigDecimal(100.00), 20, new BigDecimal(2000.00)));

        productSuppliers.add(new ProductSupplier("dummy product2", new BigDecimal(100.00), 40, new BigDecimal(4000.00)));

        productSuppliers.add(new ProductSupplier("dummy product3", new BigDecimal(100.00), 30, new BigDecimal(3000.00)));

        // dummy page
        Page<ProductSupplier> dummyProductSupplierPage = new PageImpl<>(productSuppliers, pageable, productSuppliers.size());

        // when
        when(productSupplierRepository.findAll(pageable)).thenReturn(dummyProductSupplierPage);
        // when(productSupplierService.getAll(dummyRequest, page, size)).thenReturn(productSuppliers);

        // act
        Page<ProductSupplierResponse> result = productSupplierService.getAll(dummyRequest, page, size);

        // verify
        verify(productSupplierRepository, times(1)).findAll(pageable);
        
        assertEquals(dummyProductSupplierPage.getTotalElements(), result.getTotalElements());
    }

    @Test
    void itShouldReturnSuccessWhenGetByIdProductSupplier() {
        // dummy product store
        ProductSupplier dummyProductSupplier = new ProductSupplier("dummy product", new BigDecimal(100.00), 20, new BigDecimal(2000.00));

        // when
        when(productSupplierRepository.findById(dummyProductSupplier.getId())).thenReturn(Optional.of(dummyProductSupplier));

        // act
        ProductSupplierResponse result = productSupplierService.getById(dummyProductSupplier.getId());

        // verify
        verify(productSupplierRepository, times(1)).findById(dummyProductSupplier.getId());

        // assert
        assertEquals(dummyProductSupplier.getId(), result.getProductSupplierId());
        assertEquals(dummyProductSupplier.getProductName(), result.getProductName());
        assertEquals(dummyProductSupplier.getUnitPrice(), result.getUnitPrice());
        assertEquals(dummyProductSupplier.getTotalItem(), result.getTotalItem());
        assertEquals(dummyProductSupplier.getTotalPrice(), result.getTotalPrice());

    }

    @Test
    void itShouldReturnExceptionNotFoundWhenGetByIdProductSupplier() {
        // dummy product store
        String dummyId = "dummy id";

        // when
        when(productSupplierRepository.findById(dummyId)).thenReturn(Optional.empty());

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productSupplierService.getById(dummyId));

        // verify
        verify(productSupplierRepository, times(1)).findById(dummyId);

        // assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(ResponseMessage.getNotFoundResource(ProductSupplier.class), exception.getReason());
    }

    @Test 
    void itShouldReturnSuccessWhenUpdateProductSupplier() {
        // dummy product store
        ProductSupplier dummyProductSupplier = new ProductSupplier("dummy product", new BigDecimal(100.00), 20, new BigDecimal(2000.00));

        // dummy request
        UpdateProductSupplierRequest dummyRequest = new UpdateProductSupplierRequest();
        dummyRequest.setProductName("dummy product");
        dummyRequest.setUnitPrice(new BigDecimal(100.00));
        dummyRequest.setTotalItem(20);

        // when
        when(productSupplierRepository.findById(dummyProductSupplier.getId())).thenReturn(Optional.of(dummyProductSupplier));
        when(productSupplierRepository.save(dummyProductSupplier)).thenReturn(dummyProductSupplier);

        // act
        ProductSupplierResponse result = productSupplierService.update(dummyRequest, dummyProductSupplier.getId());

        // verify
        verify(productSupplierRepository, times(1)).findById(dummyProductSupplier.getId());
        verify(productSupplierRepository, times(1)).save(dummyProductSupplier);
        verify(validationUtil, times(1)).validate(dummyRequest);

        // assert
        assertEquals(dummyProductSupplier.getId(), result.getProductSupplierId());
        assertEquals(dummyProductSupplier.getProductName(), result.getProductName());
        assertEquals(dummyProductSupplier.getUnitPrice(), result.getUnitPrice());
        assertEquals(dummyProductSupplier.getTotalItem(), result.getTotalItem());
        assertEquals(dummyProductSupplier.getTotalPrice(), result.getTotalPrice());

    }

    @Test
    void itShouldReturnExceptionNotFoundWhenUpdateProductSupplier() {
        // dummy product store
        String dummyId = "dummy id";

        // dummy request
        UpdateProductSupplierRequest dummyRequest = new UpdateProductSupplierRequest();
        dummyRequest.setProductName("dummy product");
        dummyRequest.setUnitPrice(new BigDecimal(100.00));
        dummyRequest.setTotalItem(20);

        // when
        when(productSupplierRepository.findById(dummyId)).thenReturn(Optional.empty());

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productSupplierService.update(dummyRequest, dummyId));

        // verify
        verify(productSupplierRepository, times(1)).findById(dummyId);
        verify(productSupplierRepository, never()).save(any());

        // assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(ResponseMessage.getNotFoundResource(ProductSupplier.class), exception.getReason());

    }

    @Test
    void itShouldReturnSuccesWhenDeleteProductSupplier() {
        // dummy product store
        ProductSupplier dummyProductSupplier = new ProductSupplier("dummy product", new BigDecimal(100.00), 20, new BigDecimal(2000.00));

        // when
        when(productSupplierRepository.findById(dummyProductSupplier.getId())).thenReturn(Optional.of(dummyProductSupplier));

        // act
        productSupplierService.delete(dummyProductSupplier.getId());

        // verify
        verify(productSupplierRepository, times(1)).delete(dummyProductSupplier);

    }

    @Test
    void itShouldReturnExceptionNotFoundWhenDeleteProductSupplier() {
        // dummy product store
        String dummyId = "dummy id";

        // when
        when(productSupplierRepository.findById(dummyId)).thenReturn(Optional.empty());

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productSupplierService.delete(dummyId));

        // verify
        verify(productSupplierRepository, times(1)).findById(dummyId);

        // assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(ResponseMessage.getNotFoundResource(ProductSupplier.class), exception.getReason());

    }
}
