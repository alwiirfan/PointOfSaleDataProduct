package com.pointofsale.dataSupplier.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import java.util.List;
import java.util.ArrayList;

import com.pointofsale.dataSupplier.constant.ResponseMessage;
import com.pointofsale.dataSupplier.dto.request.NewProductStoreExpirationDetailRequest;
import com.pointofsale.dataSupplier.dto.request.NewProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.response.ProductStoreExpirationDetailResponse;
import com.pointofsale.dataSupplier.dto.response.ProductStoreExpirationResponse;
import com.pointofsale.dataSupplier.dto.response.ProductStoreResponse;
import com.pointofsale.dataSupplier.entity.Category;
import com.pointofsale.dataSupplier.entity.ProductStore;
import com.pointofsale.dataSupplier.entity.ProductStoreExpiration;
import com.pointofsale.dataSupplier.entity.ProductStoreExpirationDetail;
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
    public ProductStoreExpirationResponse create(NewProductStoreExpirationRequest request) {
        // TODO validate request
        validationUtil.validate(request);

        // TODO generate of product store expiration based on request
        List<ProductStoreExpirationDetail> productStoreExpirationDetails = new ArrayList<>();

        for (NewProductStoreExpirationDetailRequest detailRequest : request.getProductStoreExpirationDetails()) {

            // TODO get product store by product store code
            ProductStore productStore = productStoreService.getByProductCode(detailRequest.getProductStoreCode());

            if (productStore == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        ResponseMessage.getBadRequest(ProductStore.class));
            }

            // TODO update product store stock after create product store expiration
            Integer newStock = newStock(productStore, detailRequest.getProductStoreExpirationTotalItem());
            productStoreService.updateProductStoreStock(newStock, productStore.getId());

            // TODO generate random string
            String randomString = randomUtil.generateRandomString(6);

            // TODO create product store expiration detail
            ProductStoreExpirationDetail productStoreExpirationDetail = ProductStoreExpirationDetail.builder()
                    .productCode(randomString)
                    .productName(detailRequest.getProductStoreExpirationName())
                    .description(detailRequest.getProductStoreExpirationDescription())
                    .merk(detailRequest.getProductStoreExpirationMerk())
                    .totalItem(detailRequest.getProductStoreExpirationTotalItem())
                    .productStore(productStore)
                    .build();

            productStoreExpirationDetail.setCreatedAt(LocalDateTime.now());

            // TODO add product store expiration detail to list
            productStoreExpirationDetails.add(productStoreExpirationDetail);
        }

        String categoryString = request.getCategory();
        if (categoryString.isEmpty() || categoryString.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    ResponseMessage.getBadRequest(Category.class));
        }

        // TODO get or add return of category
        Category category = categoryService.getCategoryByCategory(categoryString.toUpperCase());

        // TODO create product store expiration
        ProductStoreExpiration productStoreExpiration = ProductStoreExpiration.builder()
                .category(category)
                .productStoreExpirationDetails(productStoreExpirationDetails)
                .build();
            
        productStoreExpiration.setCreatedAt(LocalDateTime.now());

        productStoreExpirationRepository.saveAndFlush(productStoreExpiration);

        // TODO add product store expiration to product store expiration details
        for (ProductStoreExpirationDetail productStoreExpirationDetail : productStoreExpirationDetails) {
            productStoreExpirationDetail.setProductStoreExpiration(productStoreExpiration);
        }

        return toProductStoreExpirationResponse(productStoreExpiration);
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
    public void delete(String id) {
        ProductStoreExpiration productStoreExpiration = findByIdOrThrowNotFound(id);
        productStoreExpirationRepository.delete(productStoreExpiration);
    }

    private ProductStoreExpiration findByIdOrThrowNotFound(String id) {
        return productStoreExpirationRepository.findById(id).orElseThrow(() -> 
                new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        ResponseMessage.getNotFoundResource(ProductStoreExpiration.class)));
    }

    // TODO create initialize product store expiration response
    private ProductStoreExpirationResponse toProductStoreExpirationResponse(
            ProductStoreExpiration productStoreExpiration) {
        // TODO create product store expiration detail response based on product store expiration to list
        List<ProductStoreExpirationDetailResponse> productStoreExpirationDetailResponses = productStoreExpiration
                .getProductStoreExpirationDetails().stream().map(productStoreExpirationDetail -> {
                    
                    ProductStore productStore = productStoreExpirationDetail.getProductStore();

                    // TODO create product store response
                    ProductStoreResponse productStoreResponse = ProductStoreResponse.builder()
                        .productStoreId(productStore.getId())
                        .productCode(productStore.getProductCode())
                        .productName(productStore.getProductName())
                        .productCategory(productStore.getCategory().getCategory())
                        .productDescription(productStore.getDescription())
                        .productMerk(productStore.getMerk())
                        .productPurchasePrice(productStore.getProductPrice().getPurchasePrice())
                        .productSellingPrice(productStore.getProductPrice().getSellingPrice())
                        .productStock(productStore.getProductPrice().getStock())
                        .createdAt(productStore.getCreatedAt().toString())
                        .updatedAt(productStore.getUpdatedAt() != null ? productStore.getUpdatedAt().toString() : null)
                        .build();

                    // TODO return product store expiration detail response
                    return ProductStoreExpirationDetailResponse.builder()
                            .productStoreExpirationDetailId(productStoreExpirationDetail.getId())
                            .productStoreExpirationCode(productStoreExpirationDetail.getProductCode())
                            .productStoreExpirationName(productStoreExpirationDetail.getProductName())
                            .productStoreExpirationMerk(productStoreExpirationDetail.getMerk())
                            .productStoreExpirationTotalItem(productStoreExpirationDetail.getTotalItem())
                            .createdAt(productStoreExpirationDetail.getCreatedAt().toString())
                            .updatedAt(productStoreExpirationDetail.getUpdatedAt() != null ? productStoreExpirationDetail.getUpdatedAt().toString() : null)
                            .productStore(productStoreResponse)
                            .build();
                }).toList();

        // TODO return product store expiration response
        return ProductStoreExpirationResponse.builder()
                .productStoreExpirationId(productStoreExpiration.getId())
                .category(productStoreExpiration.getCategory().getCategory())
                .createdAt(productStoreExpiration.getCreatedAt().toString())
                .updatedAt(productStoreExpiration.getUpdatedAt() != null ? productStoreExpiration.getUpdatedAt().toString() : null)
                .productStoreExpirationDetails(productStoreExpirationDetailResponses)
                .build();
    }
    
}
