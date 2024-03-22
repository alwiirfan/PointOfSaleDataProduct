package com.pointofsale.dataSupplier.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.*;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pointofsale.dataSupplier.constant.ECategory;
import com.pointofsale.dataSupplier.dto.request.NewProductStoreRequest;
import com.pointofsale.dataSupplier.dto.response.ProductStoreResponse;
import com.pointofsale.dataSupplier.entity.Category;
import com.pointofsale.dataSupplier.entity.ProductStore;
import com.pointofsale.dataSupplier.entity.embeddable.ProductPrice;
import com.pointofsale.dataSupplier.service.CategoryService;
import com.pointofsale.dataSupplier.service.ProductStoreService;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductStoreControllerTests {
    
    @MockBean
    private ProductStoreService productStoreService;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductStore dummyProductStore;
    private ProductPrice dummyProductPrice;
    private ProductStoreResponse dummyProductStoreResponse;
    private NewProductStoreRequest dummyProductStoreRequest;
    private Category category;
    private ECategory eCategory = ECategory.HEWAN;

    @BeforeEach
    public void init() {
        // category = Category.builder().category(eCategory).build();
        // dummyProductStoreRequest = NewProductStoreRequest.builder().productCategory(eCategory.getName()).productName("product").productCode("121212").description("product remek").productStock(10).purchasePrice(new BigDecimal(100.00)).sellingPrice(new BigDecimal(200.00)).merk("sapi").build();
        dummyProductPrice = ProductPrice.builder().purchasePrice(dummyProductStoreRequest.getPurchasePrice()).sellingPrice(dummyProductStoreRequest.getSellingPrice()).stock(dummyProductStoreRequest.getProductStock()).build();
        // dummyProductStore = ProductStore.builder().productCode("121212").productName("product").description("product remek").productPrices(Collections.singletonList(dummyProductPrice)).category(category).merk("sapi").build();
        dummyProductStoreResponse = ProductStoreResponse.builder().productCode("121212").productName("product").productDescription("product remek").productCategory(eCategory.getName()).productMerk("sapi").productPurchasePrice(new BigDecimal(100.00)).productSellingPrice(new BigDecimal(200.00)).productStock(10).build();  
    }

    @Test
    void itShouldReturnSuccessWhenCreateNewProductStore() throws Exception {

        // when(categoryService.getOrSave(eCategory)).thenReturn(any(Category.class));
        when(productStoreService.createProductStore(dummyProductStoreRequest)).thenReturn(dummyProductStoreResponse);

        mockMvc.perform(
            post("/api/v1/product-store")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(dummyProductStoreRequest))
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value(201))
                    .andExpect(jsonPath("$.message").value("Successfully create product into the store"))
                    .andExpect(jsonPath("$data.productCode").value(dummyProductStore.getProductCode()))
                    .andExpect(jsonPath("$data.productName").value(dummyProductStore.getProductName()))
                    .andExpect(jsonPath("$data.productDescription").value(dummyProductStore.getDescription())
                );
      
    }
}
