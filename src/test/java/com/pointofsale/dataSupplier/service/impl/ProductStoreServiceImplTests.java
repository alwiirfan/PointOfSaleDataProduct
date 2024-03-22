package com.pointofsale.dataSupplier.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.String;
import java.util.*;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.pointofsale.dataSupplier.constant.*;
import com.pointofsale.dataSupplier.dto.request.*;
import com.pointofsale.dataSupplier.dto.response.ProductStoreResponse;
import com.pointofsale.dataSupplier.entity.*;
import com.pointofsale.dataSupplier.entity.embeddable.ProductPrice;
import com.pointofsale.dataSupplier.repository.ProductStoreRepository;
import com.pointofsale.dataSupplier.service.*;
import com.pointofsale.dataSupplier.util.ValidationUtil;

@SpringBootTest
public class ProductStoreServiceImplTests {
    
    private final ProductStoreRepository productStoreRepository = mock(ProductStoreRepository.class);
    private final ValidationUtil validationUtil = mock(ValidationUtil.class);
    private final CategoryService categoryService = mock(CategoryService.class);
    // private final ProductPriceService productPriceService = mock(ProductPriceService.class);
    // private final ProductStoreService productStoreService = new ProductStoreServiceImpl(
    //     productStoreRepository, categoryService,productPriceService, validationUtil
    //     );

    @Test
    void itShouldWhenReturnSuccessCreateProductStore() {
        // dummy request
        NewProductStoreRequest dummyRequest = new NewProductStoreRequest();
        // dummyRequest.setProductCode("121212");
        dummyRequest.setProductName("dummy product");
        dummyRequest.setDescription("dummy product store");
        dummyRequest.setProductCategory("Makanan");
        dummyRequest.setPurchasePrice(new BigDecimal(100.00));
        dummyRequest.setSellingPrice(new BigDecimal(200.00));
        dummyRequest.setProductStock(20);
        dummyRequest.setMerk("ALL");

        // dummy category
        String dummyCategory = dummyRequest.getProductCategory();
        ECategory eCategory = ECategory.getCategory(dummyCategory);
        // Category category = Category.builder().category(eCategory).build();

        // dummy product price
        ProductPrice dummyProductPrice = new ProductPrice();
        dummyProductPrice.setPurchasePrice(dummyRequest.getPurchasePrice());
        dummyProductPrice.setSellingPrice(dummyRequest.getSellingPrice());
        dummyProductPrice.setStock(dummyRequest.getProductStock());
        dummyProductPrice.setIsActive(true);

        // dummy product store
        ProductStore dummyProductStore = new ProductStore();
        // dummyProductStore.setProductCode(dummyRequest.getProductCode());
        dummyProductStore.setProductName(dummyRequest.getProductName());
        // dummyProductStore.setCategory(category);
        // dummyProductStore.setProductPrices(Collections.singletonList(dummyProductPrice));
        // dummyProductStore.setDescription(dummyRequest.getDescription());
        // dummyProductStore.setMerk(dummyRequest.getMerk());

        // // mock behavior
        // when(categoryService.getOrSave(eCategory)).thenReturn(category);
        // when(productStoreRepository.saveAndFlush(any(ProductStore.class))).thenReturn(dummyProductStore);
        
        // // Act
        // ProductStoreResponse result = productStoreService.createProductStore(dummyRequest);

        // // verivy
        // verify(categoryService, times(1) ).getOrSave(eCategory);
        // verify(productStoreRepository, times(1)).saveAndFlush(any(ProductStore.class));
        
        // // Assert product store
        // assertEquals(dummyProductStore.getId(), result.getProductStoreId());
        // assertEquals(dummyProductStore.getProductCode(), result.getProductCode());
        // assertEquals(dummyProductStore.getProductName(), result.getProductName());
        // assertEquals(dummyProductStore.getDescription(), result.getProductDescription());
        // assertEquals(dummyProductStore.getCategory().getCategory().getName(), result.getProductCategory());
        // assertEquals(dummyProductStore.getMerk(), result.getProductMerk());

        // // assert product price
        // assertEquals(dummyProductPrice.getPurchasePrice(), result.getProductPurchasePrice());
        // assertEquals(dummyProductPrice.getSellingPrice(), result.getProductSellingPrice());
        // assertEquals(dummyProductPrice.getStock(), result.getProductStock());
        // assertEquals(dummyProductPrice.getIsActive(), result.getIsActive());
    }

    @Test
    void itShouldWhenReturnSuccessGetAllProductsStore() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        
        // dummy request
        SearchProductStoreRequest request = new SearchProductStoreRequest();
        request.setProductCode("123");
        request.setProductName("ProductABC");
        request.setMinSellingPrice(50);
        request.setMaxSellingPrice(100);
        request.setPurchasePrice(30);

        List<ProductPrice> dummyProductPrices = new ArrayList<>();

        // dummyProductPrices.add(new ProductPrice(new BigDecimal(100.00), new BigDecimal(200.00), 10, 
        // new ProductStore("121212", "dummy product1", "dummy product1", 
        // new Category(ECategory.MAKANAN), dummyProductPrices, "dummy product"), true));

        // dummyProductPrices.add(new ProductPrice(new BigDecimal(200.00), new BigDecimal(400.00), 10, 
        // new ProductStore("232323", "dummy product2", "dummy product2", 
        // new Category(ECategory.MAKANAN), dummyProductPrices, "dummy product"), true));

        // dummyProductPrices.add(new ProductPrice(new BigDecimal(400.00), new BigDecimal(500.00), 10, 
        // new ProductStore("343434", "dummy product3", "dummy product3", 
        // new Category(ECategory.MAKANAN), dummyProductPrices, "dummy product"), true));

        // // mock behavior
        // Page<ProductPrice> mockProductPrices = new PageImpl<>(dummyProductPrices, pageable, dummyProductPrices.size());
        // when(productPriceService.getAllActiveProductPrice(request, page, size)).thenReturn(mockProductPrices);

        // // act
        // Page<ProductStoreResponse> result = productStoreService.getAllProductStore(request, page, size);

        // // verify
        // verify(productPriceService, times(1)).getAllActiveProductPrice(request, page, size);

        // assert
        // assertEquals(dummyProductPrices.size(), result.getTotalElements());

        for (int i = 0; i < dummyProductPrices.size(); i++) {
            // assertEquals(dummyProductPrices.get(i).getProductStore().getProductCode(), result.getContent().get(i).getProductCode());
            // assertEquals(dummyProductPrices.get(i).getProductStore().getProductName(), result.getContent().get(i).getProductName());
            // assertEquals(dummyProductPrices.get(i).getProductStore().getDescription(), result.getContent().get(i).getProductDescription());
            // assertEquals(dummyProductPrices.get(i).getProductStore().getCategory().getCategory().getName(), result.getContent().get(i).getProductCategory());
            // assertEquals(dummyProductPrices.get(i).getProductStore().getMerk(), result.getContent().get(i).getProductMerk());
            // assertEquals(dummyProductPrices.get(i).getPurchasePrice(), result.getContent().get(i).getProductPurchasePrice());
            // assertEquals(dummyProductPrices.get(i).getSellingPrice(), result.getContent().get(i).getProductSellingPrice());
            // assertEquals(dummyProductPrices.get(i).getStock(), result.getContent().get(i).getProductStock());
            // assertEquals(dummyProductPrices.get(i).getIsActive(), result.getContent().get(i).getIsActive());
        }
    }

        @Test
        void itShouldWhenReturnSuccessGetProductStoreById() {
            // dummy product price
            ProductPrice dummyProductPrice = new ProductPrice();
            dummyProductPrice.setPurchasePrice(new BigDecimal(100.00));
            dummyProductPrice.setSellingPrice(new BigDecimal(200.00));
            dummyProductPrice.setStock(10);
            dummyProductPrice.setIsActive(true);


            // dummy product store
            String productStoreId = "121212";
            ProductStore dummyProductStore = new ProductStore();
            dummyProductStore.setId(productStoreId);
            dummyProductStore.setProductCode("121212");
            dummyProductStore.setProductName("dummy product");
            dummyProductStore.setDescription("dummy product");
            // dummyProductStore.setProductPrices(Collections.singletonList(dummyProductPrice));
            // dummyProductStore.setCategory(new Category(ECategory.MAKANAN));
            dummyProductStore.setMerk("ALL");

            // mock behavior
            when(productStoreRepository.findById(dummyProductStore.getId())).thenReturn(Optional.of(dummyProductStore));

            // act
            // ProductStoreResponse result = productStoreService.getProductStoreById(dummyProductStore.getId());
            // assertNotNull(result);

            // verify
            verify(productStoreRepository, times(1)).findById(dummyProductStore.getId());

            // assert
            // assertEquals(dummyProductStore.getId(), result.getProductStoreId());
            // assertEquals(dummyProductPrice.getPurchasePrice(), result.getProductPurchasePrice());
            // assertEquals(dummyProductPrice.getSellingPrice(), result.getProductSellingPrice());
            // assertEquals(dummyProductPrice.getStock(), result.getProductStock());
            // assertEquals(dummyProductPrice.getIsActive(), result.getIsActive());
        }

        @Test
        void itShouldReturnExceptionNotFoundWhenGetProductById() {
            // dummy product price
            ProductPrice dummyProductPrice = new ProductPrice();
            dummyProductPrice.setPurchasePrice(new BigDecimal(100.00));
            dummyProductPrice.setSellingPrice(new BigDecimal(200.00));
            dummyProductPrice.setStock(10);
            dummyProductPrice.setIsActive(true);


            // dummy product store
            String productStoreId = "121212";
            ProductStore dummyProductStore = new ProductStore();
            dummyProductStore.setId(productStoreId);
            dummyProductStore.setProductCode("121212");
            dummyProductStore.setProductName("dummy product");
            dummyProductStore.setDescription("dummy product");
            // dummyProductStore.setProductPrices(Collections.singletonList(dummyProductPrice));
            // dummyProductStore.setCategory(new Category(ECategory.MAKANAN));
            dummyProductStore.setMerk("ALL");

            // mock behavior
            when(productStoreRepository.findById(dummyProductStore.getId())).thenReturn(Optional.empty());

            // act
            // ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productStoreService.getProductStoreById(dummyProductStore.getId()));
            // assertNotNull(exception);

            // verify
            verify(productStoreRepository, times(1)).findById(dummyProductStore.getId());

            // // assert
            // assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            // assertEquals(ResponseMessage.getNotFoundResource(ProductStore.class), exception.getReason());
        }
        
        @Test
        void itShouldReturnExceptionInternalServerErrorWhenGetProductStoreById() {
            // dummy product store
            String productStoreId = "121212";
            ProductStore dummyProductStore = new ProductStore();
            dummyProductStore.setId(productStoreId);
            dummyProductStore.setProductCode("121212");
            dummyProductStore.setProductName("dummy product");
            dummyProductStore.setDescription("dummy product");
            // dummyProductStore.setProductPrices(null);
            // dummyProductStore.setCategory(new Category(ECategory.MAKANAN));
            dummyProductStore.setMerk("ALL");

            // mock behavior
            when(productStoreRepository.findById(dummyProductStore.getId())).thenReturn(Optional.of(dummyProductStore));

            // act
            // ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productStoreService.getProductStoreById(dummyProductStore.getId()));
            // assertNotNull(exception);

            // verify
            verify(productStoreRepository, times(1)).findById(dummyProductStore.getId());

            // assert
            // assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
            // assertEquals(ResponseMessage.getInternalServerError(ProductPrice.class), exception.getReason());
        }

        @Test
        void itShoudReturnSuccessWhenUpdateProductStore() {
            // dummy request
            UpdateProductStoreRequest dummyRequest = new UpdateProductStoreRequest();
            dummyRequest.setProductName("dummy product X");
            dummyRequest.setDescription("dummy product store X");
            dummyRequest.setProductCategory("Makanan");
            dummyRequest.setPurchasePrice(new BigDecimal(100.00));
            dummyRequest.setSellingPrice(new BigDecimal(200.00));
            dummyRequest.setProductStock(20);
            dummyRequest.setMerk("ALL");

            // dummy category
            String dummyCategory = dummyRequest.getProductCategory();
            ECategory eCategory = ECategory.getCategory(dummyCategory);
            // Category category = Category.builder().category(eCategory).build();

            // dummy product store
            String productStoreId = "121212";
            ProductStore dummyProductStore = new ProductStore();
            dummyProductStore.setId(productStoreId);
            dummyProductStore.setProductCode("121212");
            dummyProductStore.setProductName(dummyRequest.getProductName());
            dummyProductStore.setDescription(dummyRequest.getDescription());
            // dummyProductStore.setCategory(new Category(ECategory.MAKANAN));
            dummyProductStore.setMerk("ALL");

            // dummy product price
            ProductPrice dummyProductPrice = new ProductPrice();
            dummyProductPrice.setPurchasePrice(dummyRequest.getPurchasePrice());
            dummyProductPrice.setSellingPrice(dummyRequest.getSellingPrice());
            // dummyProductPrice.setProductStore(dummyProductStore);
            dummyProductPrice.setStock(dummyRequest.getProductStock());
            dummyProductPrice.setIsActive(true);

            // mock behavior
            // when(productPriceService.getActivePriceByProductStoreId(dummyProductStore.getId())).thenReturn(dummyProductPrice);
            when(productStoreRepository.save(dummyProductStore)).thenReturn(dummyProductStore);
            // when(categoryService.getOrSave(eCategory)).thenReturn(category);
            when(productStoreRepository.findById(dummyProductStore.getId())).thenReturn(Optional.of(dummyProductStore));

            // act
            // ProductStoreResponse result = productStoreService.updateProductStore(dummyRequest, dummyProductStore.getId());
            // assertNotNull(result);

            // verify
            verify(productStoreRepository, times(1)).save(any(ProductStore.class));
            // verify(productPriceService, times(1)).getActivePriceByProductStoreId(productStoreId);
            // verify(categoryService, times(1)).getOrSave(eCategory);

            // // assert
            // assertEquals(dummyProductStore.getId(), result.getProductStoreId());
            // assertEquals(dummyProductStore.getProductCode(), result.getProductCode());
            // assertEquals(dummyProductStore.getProductName(), result.getProductName());
            // assertEquals(dummyProductStore.getDescription(), result.getProductDescription());
            // assertEquals(dummyProductStore.getCategory().getCategory().getName(), result.getProductCategory());
            // assertEquals(dummyProductStore.getMerk(), result.getProductMerk());

            // assertEquals(dummyProductStore.getId(), result.getProductStoreId());
            // assertEquals(dummyProductPrice.getPurchasePrice(), result.getProductPurchasePrice());
            // assertEquals(dummyProductPrice.getSellingPrice(), result.getProductSellingPrice());
            // assertEquals(dummyProductPrice.getStock(), result.getProductStock());
            // assertEquals(dummyProductPrice.getIsActive(), result.getIsActive());
        }

        @Test
        void itShouldReturnExceptionNotFoundWhenUpdateProductStore() {
            // dummy request
            UpdateProductStoreRequest dummyRequest = new UpdateProductStoreRequest();
            dummyRequest.setProductName("dummy product X");
            dummyRequest.setDescription("dummy product store X");
            dummyRequest.setProductCategory("Makanan");
            dummyRequest.setPurchasePrice(new BigDecimal(100.00));
            dummyRequest.setSellingPrice(new BigDecimal(200.00));
            dummyRequest.setProductStock(20);
            dummyRequest.setMerk("ALL");

            // dummy category
            String dummyCategory = dummyRequest.getProductCategory();
            ECategory eCategory = ECategory.getCategory(dummyCategory);
            // Category category = Category.builder().category(eCategory).build();

            // dummy product store
            String productStoreId = "121212";
            ProductStore dummyProductStore = new ProductStore();
            dummyProductStore.setId(productStoreId);
            dummyProductStore.setProductCode("121212");
            dummyProductStore.setProductName(dummyRequest.getProductName());
            dummyProductStore.setDescription(dummyRequest.getDescription());
            // dummyProductStore.setCategory(new Category(ECategory.MAKANAN));
            dummyProductStore.setMerk("ALL");

            // dummy product price
            ProductPrice dummyProductPrice = new ProductPrice();
            dummyProductPrice.setPurchasePrice(dummyRequest.getPurchasePrice());
            // dummyProductPrice.setSellingPrice(dummyRequest.getSellingPrice());
            // dummyProductPrice.setProductStore(null);
            dummyProductPrice.setStock(dummyRequest.getProductStock());
            dummyProductPrice.setIsActive(true);

            // mock behavior
            // when(productPriceService.getActivePriceByProductStoreId(dummyProductStore.getId())).thenReturn(dummyProductPrice);
            when(productStoreRepository.save(dummyProductStore)).thenReturn(dummyProductStore);
            // when(categoryService.getOrSave(eCategory)).thenReturn(category);
            when(productStoreRepository.findById(dummyProductStore.getId())).thenReturn(Optional.of(dummyProductStore));

            // error
            // ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productStoreService.updateProductStore(dummyRequest, dummyProductStore.getId()));

            // verify
            verify(productStoreRepository, never()).save(any());
            // verify(productPriceService, times(1)).getActivePriceByProductStoreId(productStoreId);
            // verify(categoryService, times(1)).getOrSave(eCategory);

            // assert
            // assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            // assertEquals(ResponseMessage.getNotFoundResource(ProductStore.class), exception.getReason());
        }

        @Test
        void itShouldReturnSuccessWhenDeleteProductStoreById() {
            // dummy ID
            String dummyId = "121212";

            // mock behavior
            when(productStoreRepository.findById(dummyId)).thenReturn(Optional.of(new ProductStore()));
            doNothing().when(productStoreRepository).delete(any(ProductStore.class));

            // act
            // productStoreService.deleteProductStore(dummyId);

            // verify
            verify(productStoreRepository, times(1)).findById(dummyId);
            verify(productStoreRepository, times(1)).delete(any(ProductStore.class));

        }

        @Test
        void itShouldThrowExceptionWhenProductStoreNotFound() {
            // Dummy product store ID
            String productStoreId = "nonexistentId";

            // Mock behavior
            when(productStoreRepository.findById(productStoreId)).thenReturn(Optional.empty());

            // Act and Assert
            // assertThrows(ResponseStatusException.class, () -> productStoreService.deleteProductStore(productStoreId));

            // Verify
            verify(productStoreRepository, times(1)).findById(productStoreId);
            verify(productStoreRepository, never()).delete(any(ProductStore.class));
}
}
