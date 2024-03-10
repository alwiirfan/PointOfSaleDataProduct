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
import com.pointofsale.dataSupplier.util.ValidationUtil;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

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


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductStoreResponse createProductStore(NewProductStoreRequest request) {
        validationUtil.validate(request);
        try {
            String categoryString = request.getProductCategory();
            if (categoryString.isEmpty() || categoryString.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        ResponseMessage.getBadRequest(Category.class));
            }

            Category category = categoryService.getCategoryByECategory(categoryString.toUpperCase());

            ProductStore productStore = ProductStore.builder()
                    .productCode(request.getProductCode())
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

            productStoreRepository.saveAndFlush(productStore);

            return toProductStoreResponse(productStore);

        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    ResponseMessage.getDuplicateResource(ProductStore.class));
        }

    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductStoreResponse> getAllProductStore(SearchProductStoreRequest request, Integer page, Integer size) {
        Specification<ProductStore> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // by product store code
            if (Objects.nonNull(request.getProductCode())){
                predicates.add(criteriaBuilder
                            .equal(root.get("productCode"), request.getProductCode()));
            }

            // by product store name
            if (Objects.nonNull(request.getProductName())){
                predicates.add(criteriaBuilder
                            .like(criteriaBuilder.lower(root.get("productName")),
                                            "%" + request.getProductName().toLowerCase() + "%"));
            }

            // by min selling price
            if (Objects.nonNull(request.getMinSellingPrice())){
                predicates.add(criteriaBuilder
                            .greaterThanOrEqualTo(root.get("sellingPrice"), request.getMinSellingPrice()));
            }

            // by max selling price
            if (Objects.nonNull(request.getMaxSellingPrice())){
                predicates.add(criteriaBuilder
                            .lessThanOrEqualTo(root.get("sellingPrice"), request.getMaxSellingPrice()));
            }

            // by purchase price
            if (Objects.nonNull(request.getPurchasePrice())){
                predicates.add(criteriaBuilder
                            .greaterThanOrEqualTo(root.get("purchasePrice"), request.getPurchasePrice()));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(page, size);
        
        Page<ProductStore> productStores = productStoreRepository.findAll(specification, pageable);

        List<ProductStoreResponse> responses = productStores.stream().map(productStore -> {
            return toProductStoreResponse(productStore);
        }).collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, productStores.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public ProductStoreResponse getProductStoreById(String id) {
        ProductStore productStore = productStoreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            ResponseMessage.getNotFoundResource(ProductStore.class)));

        return toProductStoreResponse(productStore);
    }

    private ProductStore findByIdOrThrowNotFound(String id) {
        return productStoreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                            ResponseMessage.getNotFoundResource(ProductStore.class)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductStoreResponse updateProductStore(UpdateProductStoreRequest request, String id) {
        validationUtil.validate(request);

        try {
            String categoryString = request.getProductCategory();
            if (categoryString.isEmpty() || categoryString.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                            ResponseMessage.getBadRequest(Category.class));
            }

            Category category = categoryService.getCategoryByECategory(categoryString.toUpperCase());

            ProductStore productStore = findByIdOrThrowNotFound(id);
            
            productStore.setProductName(request.getProductName());
            productStore.setCategory(category);
            productStore.setDescription(request.getDescription());
            productStore.setMerk(request.getMerk());
            productStore.setProductPrice(ProductPrice.builder()
                    .stock(request.getProductStock())
                    .purchasePrice(request.getPurchasePrice())
                    .sellingPrice(request.getSellingPrice())
                    .isActive(request.isActive())
                    .build());

            productStoreRepository.save(productStore);
            return toProductStoreResponse(productStore);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    ResponseMessage.getDuplicateResource(ProductStore.class));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteProductStore(String id) {
        ProductStore productStore = findByIdOrThrowNotFound(id);
        productStoreRepository.delete(productStore);
    }

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
                .productMerk(productStore.getMerk())
                .build();
    }

    @Override
    public Page<ProductStoreResponse> getAllProductStoreByCategory(String category, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ProductStore> productStores = productStoreRepository.findAllProductStoreByCategory(category.toUpperCase(), pageable);

        List<ProductStoreResponse> responses = productStores.stream().map(this::toProductStoreResponse).collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, productStores.getTotalElements());
    }
}
