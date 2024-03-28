package com.pointofsale.dataSupplier.service.impl;

import com.pointofsale.dataSupplier.constant.ResponseMessage;
import com.pointofsale.dataSupplier.dto.request.NewProductStoreRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductStoreRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateProductStoreRequest;
import com.pointofsale.dataSupplier.dto.response.ProductStoreResponse;
import com.pointofsale.dataSupplier.entity.Category;
import com.pointofsale.dataSupplier.entity.ProductStore;
import com.pointofsale.dataSupplier.entity.embeddable.ProductPrice;
import com.pointofsale.dataSupplier.repository.ProductStoreRepository;
import com.pointofsale.dataSupplier.service.CategoryService;
import com.pointofsale.dataSupplier.service.ProductStoreService;
import com.pointofsale.dataSupplier.util.RandomUtil;
import com.pointofsale.dataSupplier.util.ValidationUtil;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductStoreServiceImpl implements ProductStoreService {

    private final ProductStoreRepository productStoreRepository;
    private final CategoryService categoryService;
    private final ValidationUtil validationUtil;
    private final RandomUtil randomUtil;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductStoreResponse createProductStore(NewProductStoreRequest request) {
        // TODO validate request
        validationUtil.validate(request);
        try {
            String categoryString = request.getProductCategory();
            if (categoryString.isEmpty() || categoryString.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        ResponseMessage.getBadRequest(Category.class));
            }

            // TODO get or add return of category
            Category category = categoryService.getCategoryByCategory(categoryString.toUpperCase());

            // TODO generate random of product store code
            String productCode = randomUtil.generateRandomString(6);

            // TODO create product store
            ProductStore productStore = ProductStore.builder()
                    .productCode(productCode)
                    .productName(request.getProductName())
                    .category(category)
                    .description(request.getDescription())
                    .productPrice(ProductPrice.builder()
                            .purchasePrice(request.getPurchasePrice())
                            .sellingPrice(request.getSellingPrice())
                            .stock(request.getProductStock())
                            .isActive(true)
                            .build())
                    .merk(request.getMerk())
                    .build();

            productStore.setCreatedAt(LocalDateTime.now());

            productStoreRepository.saveAndFlush(productStore);

            // TODO return response
            return toProductStoreResponse(productStore);

        }catch (IllegalArgumentException e){
            // TODO throw exception
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    ResponseMessage.getDuplicateResource(ProductStore.class));
        }

    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductStoreResponse> getAllProductStore(SearchProductStoreRequest request, Integer page, Integer size) {
        // TODO create specification
        Specification<ProductStore> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // TODO filter by product store code
            if (Objects.nonNull(request.getProductCode())){
                predicates.add(criteriaBuilder
                            .equal(root.get("productCode"), request.getProductCode()));
            }

            // TODO filter by product store name
            if (Objects.nonNull(request.getProductName())){
                predicates.add(criteriaBuilder
                            .like(criteriaBuilder.lower(root.get("productName")),
                                            "%" + request.getProductName().toLowerCase() + "%"));
            }

            // TODO filter by category
            if (Objects.nonNull(request.getCategory())){
                predicates.add(criteriaBuilder
                            .like(criteriaBuilder.upper(root.join("category").get("category")),
                                     "%" + request.getCategory().toUpperCase() + "%"));
            }

            // TODO filter by merk
            if (Objects.nonNull(request.getMerk())) {
                predicates.add(criteriaBuilder
                            .equal(criteriaBuilder.lower(root.get("merk")), request.getMerk().toLowerCase()));
            }

            // TODO filter by min selling price
            if (Objects.nonNull(request.getMinSellingPrice())){
                predicates.add(criteriaBuilder
                            .greaterThanOrEqualTo(root.get("sellingPrice"), request.getMinSellingPrice()));
            }

            // TODO filter by max selling price
            if (Objects.nonNull(request.getMaxSellingPrice())){
                predicates.add(criteriaBuilder
                            .lessThanOrEqualTo(root.get("sellingPrice"), request.getMaxSellingPrice()));
            }

            // TODO filter by purchase price
            if (Objects.nonNull(request.getPurchasePrice())){
                predicates.add(criteriaBuilder
                            .greaterThanOrEqualTo(root.get("purchasePrice"), request.getPurchasePrice()));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        // TODO create pageable
        Pageable pageable = PageRequest.of(page, size);
        
        // TODO get all product store
        Page<ProductStore> productStores = productStoreRepository.findAll(specification, pageable);

        // TODO map responses to list
        List<ProductStoreResponse> responses = productStores.stream().map(productStore -> {
            return toProductStoreResponse(productStore);
        }).collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, productStores.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public ProductStoreResponse getProductStoreByProductStoreCode(String productStoreCode) {
        // TODO get product store by product store code
        ProductStore productStore = productStoreRepository.findFirstByProductCode(productStoreCode)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                            ResponseMessage.getNotFoundResource(ProductStore.class)));

        // TODO return response
        return toProductStoreResponse(productStore);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductStoreResponse getProductStoreById(String id) {
        // TODO get product store by id
        ProductStore productStore = productStoreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            ResponseMessage.getNotFoundResource(ProductStore.class)));

        // TODO return response
        return toProductStoreResponse(productStore);
    }

    private ProductStore findByIdOrThrowNotFound(String id) {
        // TODO get product store by id or else return not found
        return productStoreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                            ResponseMessage.getNotFoundResource(ProductStore.class)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductStoreResponse updateProductStore(UpdateProductStoreRequest request, String id) {
        // TODO validate request
        validationUtil.validate(request);

        try {
            String categoryString = request.getProductCategory();
            if (categoryString.isEmpty() || categoryString.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                            ResponseMessage.getBadRequest(Category.class));
            }

            // TODO get or add return of category
            Category category = categoryService.getCategoryByCategory(categoryString.toUpperCase());

            // TODO get product store by id
            ProductStore productStore = findByIdOrThrowNotFound(id);
            
            // TODO update product store
            productStore.setProductName(request.getProductName());
            productStore.setCategory(category);
            productStore.setDescription(request.getDescription());
            productStore.setMerk(request.getMerk());
            productStore.setProductPrice(ProductPrice.builder()
                    .stock(request.getProductStock())
                    .purchasePrice(request.getPurchasePrice())
                    .sellingPrice(request.getSellingPrice())
                    .isActive(request.getIsActive())
                    .build());

            productStore.setUpdatedAt(LocalDateTime.now());

            productStoreRepository.save(productStore);

            // TODO return response
            return toProductStoreResponse(productStore);
        } catch (IllegalArgumentException e) {
            // TODO throw exception
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    ResponseMessage.getDuplicateResource(ProductStore.class));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteProductStore(String id) {
        // TODO get product store by id
        ProductStore productStore = findByIdOrThrowNotFound(id);

        // TODO delete product store
        productStoreRepository.delete(productStore);
    }

    @Override
    public ProductStore getByProductCode(String productCode) {
        // TODO get product store by product code or else return not found
        return productStoreRepository.findFirstByProductCode(productCode).orElseThrow(() -> 
                        new ResponseStatusException(HttpStatus.NOT_FOUND, 
                                ResponseMessage.getNotFoundResource(ProductStore.class)));
    }

    @Override
    public ProductStore updateProductStoreStock(Integer stock, String id) {
        // TODO get product store by id
        ProductStore productStore = findByIdOrThrowNotFound(id);

        // TODO update product store stock
        ProductPrice productPrice = productStore.getProductPrice();

        productPrice.setStock(stock);

        productStore.setUpdatedAt(LocalDateTime.now());

        // TODO save product store
        return productStoreRepository.save(productStore);
    }

    // TODO create product store response
    private ProductStoreResponse toProductStoreResponse(ProductStore productStore) {
        return ProductStoreResponse.builder()
                .productStoreId(productStore.getId())
                .productCode(productStore.getProductCode())
                .productName(productStore.getProductName())
                .productCategory(productStore.getCategory().getCategory())
                .productDescription(productStore.getDescription())
                .productPurchasePrice(productStore.getProductPrice().getPurchasePrice())
                .productSellingPrice(productStore.getProductPrice().getSellingPrice())
                .productStock(productStore.getProductPrice().getStock())
                .isActive(productStore.getProductPrice().getIsActive())
                .productMerk(productStore.getMerk())
                .createdAt(productStore.getCreatedAt().toString())
                .updatedAt(productStore.getUpdatedAt() != null ? productStore.getUpdatedAt().toString() : null)
                .build();
    }

}
