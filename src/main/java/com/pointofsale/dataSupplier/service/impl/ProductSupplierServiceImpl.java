package com.pointofsale.dataSupplier.service.impl;

import com.pointofsale.dataSupplier.constant.ResponseMessage;
import com.pointofsale.dataSupplier.dto.request.NewProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.response.ProductSupplierResponse;
import com.pointofsale.dataSupplier.entity.ProductSupplier;
import com.pointofsale.dataSupplier.repository.ProductSupplierRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductSupplierServiceImpl implements ProductSupplierService {

    private final ProductSupplierRepository productSupplierRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductSupplierResponse create(NewProductSupplierRequest request) {
        validationUtil.validate(request);

        BigDecimal unitPrice = request.getUnitPrice();
        Integer totalItem = request.getTotalItem();

        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(totalItem));

        ProductSupplier productSupplier = ProductSupplier.builder()
                .productName(request.getProductName())
                .unitPrice(unitPrice)
                .totalItem(totalItem)
                .totalPrice(totalPrice)
                .build();

        productSupplierRepository.save(productSupplier);

        return toProductSupplierResponse(productSupplier);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductSupplierResponse> getAll(SearchProductSupplierRequest request, Integer page, Integer size) {
        Specification<ProductSupplier> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // product supplier name
            if (Objects.nonNull(request.getProductName())){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" +request.getProductName().toLowerCase()+ "%"));
            }

            // total item
            if (Objects.nonNull(request.getTotalItem())){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalItem"), request.getTotalItem()));
            }

            // minimum unit price
            if (Objects.nonNull(request.getMinUnitPrice())){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("unitPrice"), request.getMinUnitPrice()));
            }

            // minimum total price
            if (Objects.nonNull(request.getMinTotalPrice())){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalPrice"), request.getMinTotalPrice()));
            }

            // maximum unit price
            if (Objects.nonNull(request.getMaxUnitPrice())){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("unitPrice"), request.getMaxUnitPrice()));
            }

            // maximum total price
            if (Objects.nonNull(request.getMaxTotalPrice())){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), request.getMaxTotalPrice()));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductSupplier> productSuppliers = productSupplierRepository.findAll(specification, pageable);

        List<ProductSupplierResponse> responses = productSuppliers.stream().map(this::toProductSupplierResponse).toList();

        return new PageImpl<>(responses, pageable, productSuppliers.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public ProductSupplierResponse getById(String id) {
        ProductSupplier productSupplier = productSupplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ResponseMessage.getNotFoundResource(ProductSupplier.class)));

        return toProductSupplierResponse(productSupplier);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductSupplierResponse update(UpdateProductSupplierRequest request, String id) {
        validationUtil.validate(request);

        ProductSupplier productSupplier = productSupplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ResponseMessage.getNotFoundResource(ProductSupplier.class)));

        if (productSupplier != null) {
            Integer totalItem = request.getTotalItem();
            BigDecimal unitPrice = request.getUnitPrice();

            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(totalItem));

            productSupplier.setProductName(request.getProductName());
            productSupplier.setUnitPrice(request.getUnitPrice());
            productSupplier.setTotalItem(request.getTotalItem());
            productSupplier.setTotalPrice(totalPrice);


            productSupplierRepository.save(productSupplier);

            return toProductSupplierResponse(productSupplier);
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        ProductSupplier productSupplier = productSupplierRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ResponseMessage.getNotFoundResource(ProductSupplier.class)));

        productSupplierRepository.delete(productSupplier);
    }

    private ProductSupplierResponse toProductSupplierResponse(ProductSupplier productSupplier) {
        return ProductSupplierResponse.builder()
                .productSupplierId(productSupplier.getId())
                .productName(productSupplier.getProductName())
                .unitPrice(productSupplier.getUnitPrice())
                .totalItem(productSupplier.getTotalItem())
                .totalPrice(productSupplier.getTotalPrice())
                .build();
    }
}
