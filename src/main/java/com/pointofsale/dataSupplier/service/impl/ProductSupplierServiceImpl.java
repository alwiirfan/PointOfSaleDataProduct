package com.pointofsale.dataSupplier.service.impl;

import com.pointofsale.dataSupplier.constant.ResponseMessage;
import com.pointofsale.dataSupplier.dto.request.NewProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.response.ProductSupplierResponse;
import com.pointofsale.dataSupplier.entity.Category;
import com.pointofsale.dataSupplier.entity.ProductSupplier;
import com.pointofsale.dataSupplier.repository.ProductSupplierRepository;
import com.pointofsale.dataSupplier.service.CategoryService;
import com.pointofsale.dataSupplier.service.ProductSupplierService;
import com.pointofsale.dataSupplier.util.ValidationUtil;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductSupplierServiceImpl implements ProductSupplierService {

    private final ProductSupplierRepository productSupplierRepository;
    private final CategoryService categoryService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductSupplierResponse create(NewProductSupplierRequest request) {
        // TODO validate request
        validationUtil.validate(request);

        try {
            String categoryString = request.getCategory();
            if (categoryString.isEmpty() || categoryString.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        ResponseMessage.getBadRequest(Category.class));
            }

            // TODO get or add return of category
            Category category = categoryService.getCategoryByCategory(categoryString.toUpperCase());

            BigDecimal unitPrice = request.getUnitPrice();
            Integer totalItem = request.getTotalItem();

            // TODO calculate total price
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(totalItem));

            // TODO create product supplier
            ProductSupplier productSupplier = ProductSupplier.builder()
                    .productName(request.getProductName())
                    .category(category)
                    .unitPrice(unitPrice)
                    .totalItem(totalItem)
                    .totalPrice(totalPrice)
                    .merk(request.getMerk())
                    .build();

            productSupplier.setCreatedAt(LocalDateTime.now());

            productSupplierRepository.save(productSupplier);

            // TODO return response of product supplier
            return toProductSupplierResponse(productSupplier);
        } catch (IllegalArgumentException e) {
            // TODO throw exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductSupplierResponse> getAll(SearchProductSupplierRequest request, Integer page, Integer size) {
        // TODO create specification based on request
        Specification<ProductSupplier> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // TODO filter by product supplier name
            if (Objects.nonNull(request.getProductName())){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), 
                        "%" +request.getProductName().toUpperCase()+ "%"));
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

            // TODO filter by total item
            if (Objects.nonNull(request.getTotalItem())){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalItem"), request.getTotalItem()));
            }

            // TODO filter by minimum unit price
            if (Objects.nonNull(request.getMinUnitPrice())){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("unitPrice"), request.getMinUnitPrice()));
            }

            // TODO filter by minimum total price
            if (Objects.nonNull(request.getMinTotalPrice())){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalPrice"), request.getMinTotalPrice()));
            }

            // TODO filter by maximum unit price
            if (Objects.nonNull(request.getMaxUnitPrice())){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("unitPrice"), request.getMaxUnitPrice()));
            }

            // TODO filter by maximum total price
            if (Objects.nonNull(request.getMaxTotalPrice())){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), request.getMaxTotalPrice()));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        // TODO create pageable
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductSupplier> productSuppliers = productSupplierRepository.findAll(specification, pageable);

        // TODO create product supplier response to list
        List<ProductSupplierResponse> responses = productSuppliers.stream().map(this::toProductSupplierResponse).toList();

        return new PageImpl<>(responses, pageable, productSuppliers.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public ProductSupplierResponse getById(String id) {
        // TODO get product supplier by id
        ProductSupplier productSupplier = productSupplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ResponseMessage.getNotFoundResource(ProductSupplier.class)));

        return toProductSupplierResponse(productSupplier);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductSupplierResponse update(UpdateProductSupplierRequest request, String id) {
        // TODO validate request
        validationUtil.validate(request);

        try {
            // TODO get product supplier by id
            ProductSupplier productSupplier = productSupplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ResponseMessage.getNotFoundResource(ProductSupplier.class)));

            // TODO if product supplier is present then update category
            String categoryString = request.getCategory();
            if (categoryString.isEmpty() || categoryString.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        ResponseMessage.getBadRequest(Category.class));
            }

            Category category = categoryService.getCategoryByCategory(categoryString.toUpperCase());

            // TODO if product supplier is present then update product supplier
        if (productSupplier != null) {
            Integer totalItem = request.getTotalItem();
            BigDecimal unitPrice = request.getUnitPrice();

            // TODO calculate total price
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(totalItem));

            productSupplier.setProductName(request.getProductName());
            productSupplier.setCategory(category);
            productSupplier.setUnitPrice(request.getUnitPrice());
            productSupplier.setTotalItem(request.getTotalItem());
            productSupplier.setTotalPrice(totalPrice);
            productSupplier.setMerk(request.getMerk());

            productSupplier.setUpdatedAt(LocalDateTime.now());

            productSupplierRepository.save(productSupplier);
        }

        // TODO return response of product supplier
            return toProductSupplierResponse(productSupplier);
        } catch (IllegalArgumentException e) {
            // TODO throw exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        // TODO get product supplier by id
        ProductSupplier productSupplier = productSupplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ResponseMessage.getNotFoundResource(ProductSupplier.class)));

        // TODO delete product supplier
        productSupplierRepository.delete(productSupplier);
    }

    // TODO create product supplier response
    private ProductSupplierResponse toProductSupplierResponse(ProductSupplier productSupplier) {
        return ProductSupplierResponse.builder()
                .productSupplierId(productSupplier.getId())
                .productName(productSupplier.getProductName())
                .category(productSupplier.getCategory().getCategory())
                .unitPrice(productSupplier.getUnitPrice())
                .totalItem(productSupplier.getTotalItem())
                .totalPrice(productSupplier.getTotalPrice())
                .merk(productSupplier.getMerk())
                .createdAt(productSupplier.getCreatedAt().toString())
                .updatedAt(productSupplier.getUpdatedAt() != null ? productSupplier.getUpdatedAt().toString() : null)
                .build();
    }
}
