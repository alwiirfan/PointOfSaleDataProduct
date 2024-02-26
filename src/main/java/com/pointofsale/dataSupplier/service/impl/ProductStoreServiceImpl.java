package com.pointofsale.dataSupplier.service.impl;

import com.pointofsale.dataSupplier.constant.ECategory;
import com.pointofsale.dataSupplier.constant.ResponseMessage;
import com.pointofsale.dataSupplier.dto.request.NewProductStoreRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductStoreRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateProductStoreRequest;
import com.pointofsale.dataSupplier.dto.response.ProductStoreResponse;
import com.pointofsale.dataSupplier.entity.Category;
import com.pointofsale.dataSupplier.entity.ProductPrice;
import com.pointofsale.dataSupplier.entity.ProductStore;
import com.pointofsale.dataSupplier.repository.ProductStoreRepository;
import com.pointofsale.dataSupplier.service.CategoryService;
import com.pointofsale.dataSupplier.service.ProductPriceService;
import com.pointofsale.dataSupplier.service.ProductStoreService;
import com.pointofsale.dataSupplier.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ProductStoreServiceImpl implements ProductStoreService {

    private final ProductStoreRepository productStoreRepository;
    private final CategoryService categoryService;
    private final ProductPriceService productPriceService;
    private final ValidationUtil validationUtil;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductStoreResponse createProductStore(NewProductStoreRequest request) {
        validationUtil.validate(request);
        try {
            String productCategoryString = request.getProductCategory();
            if (productCategoryString.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.getBadRequest(ProductStore.class));
            }

            ECategory eCategory = ECategory.getCategory(productCategoryString);
            Category category = categoryService.getOrSave(eCategory);

            ProductPrice productPrice = ProductPrice.builder()
                    .purchasePrice(request.getPurchasePrice())
                    .sellingPrice(request.getSellingPrice())
                    .stock(request.getProductStock())
                    .isActive(true)
                    .build();

            ProductStore productStore = ProductStore.builder()
                    .productCode(request.getProductCode())
                    .productName(request.getProductName())
                    .category(category)
                    .description(request.getDescription())
                    .productPrices(Collections.singletonList(productPrice))
                    .merk(request.getMerk())
                    .build();

            productPrice.setProductStore(productStore);

            productStoreRepository.saveAndFlush(productStore);

            return toProductStoreResponse(productStore, productPrice);

        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, ResponseMessage.getDuplicateResource(ProductStore.class));
        }

    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductStoreResponse> getAllProductStore(SearchProductStoreRequest request, Integer page, Integer size) {
        SearchProductStoreRequest searchProductStoreRequest = SearchProductStoreRequest.builder()
                .maxSellingPrice(request.getMaxSellingPrice())
                .minSellingPrice(request.getMinSellingPrice())
                .productCode(request.getProductCode())
                .productName(request.getProductName())
                .purchasePrice(request.getPurchasePrice())
                .build();

        Page<ProductPrice> allActiveProductPrice = productPriceService.getAllActiveProductPrice(searchProductStoreRequest, page, size);
        return allActiveProductPrice.map(productPrice -> toProductStoreResponse(productPrice.getProductStore(), productPrice));
    }

    @Transactional(readOnly = true)
    @Override
    public ProductStoreResponse getProductStoreById(String id) {
        ProductStore productStore = productStoreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.getNotFoundResource(ProductStore.class)));

        return toProductStoreResponse(productStore, productStore.getProductPrices().get(0));
    }

    private ProductStore findByIdOrThrowNotFound(String id) {
        return productStoreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.getNotFoundResource(ProductStore.class)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductStoreResponse updateProductStore(UpdateProductStoreRequest request, String id) {
        validationUtil.validate(request);
        ProductPrice productPrice = productPriceService.getActivePriceByProductStoreId(id);

        String productCategoryString = request.getProductCategory();
        if (productCategoryString.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.getBadRequest(ProductStore.class));
        }

        ECategory eCategory = ECategory.getCategory(productCategoryString);
        Category category = categoryService.getOrSave(eCategory);

        ProductStore productStore = productPrice.getProductStore();
        productStore.setProductName(request.getProductName());
        productStore.setCategory(category);
        productStore.setDescription(request.getDescription());
        productStore.setMerk(request.getMerk());

        ProductPrice.builder()
                .productStore(productStore)
                .stock(request.getProductStock())
                .purchasePrice(request.getPurchasePrice())
                .sellingPrice(request.getSellingPrice())
                .isActive(true)
                .build();

        productStoreRepository.save(productStore);
        return toProductStoreResponse(productStore, productPrice);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteProductStore(String id) {
        ProductStore productStore = findByIdOrThrowNotFound(id);
        productStoreRepository.delete(productStore);
    }

    private ProductStoreResponse toProductStoreResponse(ProductStore productStore, ProductPrice productPrice) {
        return ProductStoreResponse.builder()
                .productStoreId(productStore.getId())
                .productPriceId(productPrice.getId())
                .productCode(productStore.getProductCode())
                .productName(productStore.getProductName())
                .productCategory(productStore.getCategory().getCategory().getName())
                .productDescription(productStore.getDescription())
                .productPurchasePrice(productPrice.getPurchasePrice())
                .productSellingPrice(productPrice.getSellingPrice())
                .productStock(productPrice.getStock())
                .productMerk(productStore.getMerk())
                .build();
    }
}
