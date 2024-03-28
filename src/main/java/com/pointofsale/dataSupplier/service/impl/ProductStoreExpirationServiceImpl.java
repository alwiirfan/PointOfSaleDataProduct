package com.pointofsale.dataSupplier.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import com.pointofsale.dataSupplier.constant.ResponseMessage;
import com.pointofsale.dataSupplier.dto.request.NewProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.response.ProductStoreExpirationResponse;
import com.pointofsale.dataSupplier.dto.response.ProductStoreResponse;
import com.pointofsale.dataSupplier.entity.Category;
import com.pointofsale.dataSupplier.entity.ProductStore;
import com.pointofsale.dataSupplier.entity.ProductStoreExpiration;
import com.pointofsale.dataSupplier.repository.ProductStoreExpirationRepository;
import com.pointofsale.dataSupplier.service.CategoryService;
import com.pointofsale.dataSupplier.service.ProductStoreExpirationService;
import com.pointofsale.dataSupplier.service.ProductStoreService;
import com.pointofsale.dataSupplier.util.RandomUtil;
import com.pointofsale.dataSupplier.util.ValidationUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductStoreExpirationServiceImpl implements ProductStoreExpirationService {
    
    private final ProductStoreExpirationRepository productStoreExpirationRepository;
    private final CategoryService categoryService;
    private final ProductStoreService productStoreService;
    private final ValidationUtil validationUtil;
    private final RandomUtil randomUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductStoreExpirationResponse create(NewProductStoreExpirationRequest request, String productStoreCode) {
        // TODO validate request
        validationUtil.validate(request);
        try {
            // TODO get product store by product store code
            ProductStore productStore = productStoreService.getByProductCode(productStoreCode);

            if (productStore == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        ResponseMessage.getBadRequest(ProductStore.class));
            }

            // TODO update stock in product store
            Integer newStock = newStock(productStore, request.getStock());
            productStoreService.updateProductStoreStock(newStock, productStore.getId());
            
            // TODO validate category
            String categoryString = request.getCategory();
            if (categoryString.isEmpty() || categoryString.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        ResponseMessage.getBadRequest(Category.class));
            }

            // TODO get or add return of category
            Category category = categoryService.getCategoryByCategory(categoryString.toUpperCase());

            // TODO generate random of product store code
            String productCode = randomUtil.generateRandomString(6);

            // TODO create product store expiration
            ProductStoreExpiration productStoreExpiration = ProductStoreExpiration.builder()
                    .productCode(productCode)
                    .productName(request.getProductName())
                    .description(request.getDescription())
                    .category(category)
                    .merk(request.getMerk())
                    .stock(request.getStock())
                    .productStore(productStore)
                    .build();

            productStoreExpiration.setCreatedAt(LocalDateTime.now());

            productStore.setProductStoreExpiration(productStoreExpiration);

            // TODO save and flush
            productStoreExpirationRepository.saveAndFlush(productStoreExpiration);

            return toCreateProductStoreEpirationResponse(productStoreExpiration);
        } catch (Exception e) {
            // TODO throw exception
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    ResponseMessage.getDuplicateResource(ProductStoreExpiration.class));
        }
    }

    private Integer newStock(ProductStore productStore, Integer totalItemProductStoreExpiration) {
        Integer oldStock = productStore.getProductPrice().getStock();

        if (oldStock < totalItemProductStoreExpiration) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock not enough");
        }

        Integer newStock = oldStock - totalItemProductStoreExpiration;
        return newStock;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductStoreExpirationResponse> getAll(SearchProductStoreExpirationRequest request, Integer page,
            Integer size) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductStoreExpiration update(UpdateProductStoreExpirationRequest request, String productStoreCode,
            String productStoreExpirationCode) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        ProductStoreExpiration productStoreExpiration = findByIdOrThrowNotFound(id);
        productStoreExpirationRepository.delete(productStoreExpiration);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductStoreExpiration getByProductStoreExpirationCode(String productStoreExpirationCode) {
        // TODO get by product store expiration by code
        return productStoreExpirationRepository.findFirstByProductCode(productStoreExpirationCode).orElseThrow(() -> 
                new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        ResponseMessage.getNotFoundResource(ProductStoreExpiration.class)));
    }

    private ProductStoreExpiration findByIdOrThrowNotFound(String id) {
        return productStoreExpirationRepository.findById(id).orElseThrow(() -> 
                new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        ResponseMessage.getNotFoundResource(ProductStoreExpiration.class)));
    }

    private ProductStoreExpirationResponse toCreateProductStoreEpirationResponse(
            ProductStoreExpiration productStoreExpiration) {
        return ProductStoreExpirationResponse.builder()
                .productStoreExpirationId(productStoreExpiration.getId())
                .productCode(productStoreExpiration.getProductCode())
                .productName(productStoreExpiration.getProductName())
                .description(productStoreExpiration.getDescription())
                .category(productStoreExpiration.getCategory().getCategory())
                .merk(productStoreExpiration.getMerk())
                .stock(productStoreExpiration.getStock())
                .productStore(ProductStoreResponse.builder()
                        .productStoreId(productStoreExpiration.getProductStore().getId())
                        .productCode(productStoreExpiration.getProductStore().getProductCode())
                        .productName(productStoreExpiration.getProductStore().getProductName())
                        .productCategory(productStoreExpiration.getProductStore().getCategory().getCategory())
                        .productDescription(productStoreExpiration.getProductStore().getDescription())
                        .productPurchasePrice(productStoreExpiration.getProductStore().getProductPrice().getPurchasePrice())
                        .productSellingPrice(productStoreExpiration.getProductStore().getProductPrice().getSellingPrice())
                        .productStock(productStoreExpiration.getProductStore().getProductPrice().getStock())
                        .productMerk(productStoreExpiration.getProductStore().getMerk())
                        .createdAt(productStoreExpiration.getProductStore().getCreatedAt().toString())
                        .updatedAt(productStoreExpiration.getProductStore().getUpdatedAt() != null ? 
                                productStoreExpiration.getProductStore().getUpdatedAt().toString() : null)
                        .build())
                .createdAt(productStoreExpiration.getCreatedAt().toString())
                .updatedAt(productStoreExpiration.getUpdatedAt() != null ? productStoreExpiration.getUpdatedAt().toString() : null)
                .build();
    }
    
}
