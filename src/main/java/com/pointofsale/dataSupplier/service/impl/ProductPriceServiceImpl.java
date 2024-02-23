package com.pointofsale.dataSupplier.service.impl;

import com.pointofsale.dataSupplier.constant.ResponseMessage;
import com.pointofsale.dataSupplier.dto.request.SearchProductStoreRequest;
import com.pointofsale.dataSupplier.entity.ProductPrice;
import com.pointofsale.dataSupplier.repository.ProductPriceRepository;
import com.pointofsale.dataSupplier.service.ProductPriceService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductPriceServiceImpl implements ProductPriceService {

    private final ProductPriceRepository productPriceRepository;

    @Override
    public List<ProductPrice> getAllProductPriceByProductStoreCode(String productStoreCode) {
        return productPriceRepository.findAllByProductStore_ProductCode(productStoreCode);
    }

    @Override
    public ProductPrice getActivePriceByProductStoreId(String productStoreId) {
        return productPriceRepository.findFirstByIsActiveTrueAndProductStore_Id(productStoreId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                            ResponseMessage.getNotFoundResource(ProductPrice.class)));
    }

    @Override
    public ProductPrice getProductPriceById(String productPriceId) {
        return productPriceRepository.findById(productPriceId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ResponseMessage.getNotFoundResource(ProductPrice.class)));
    }

    @Override
    public ProductPrice getActivePriceByProductStoreCode(String productStoreCode) {
        return productPriceRepository.findFirstByIsActiveTrueAndProductStore_ProductCode(productStoreCode)
                .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            ResponseMessage.getNotFoundResource(ProductPrice.class)));
    }

    @Override
    public Page<ProductPrice> getAllActiveProductPrice(SearchProductStoreRequest request, Integer page, Integer size) {
        Specification<ProductPrice> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // by product store code
            if (Objects.nonNull(request.getProductCode())){
                predicates.add(criteriaBuilder.equal(root.join("productStore").get("productCode"), request.getProductCode()));
            }

            // by product store name
            if (Objects.nonNull(request.getProductName())){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("productStore").get("productName")),
                        "%" + request.getProductName().toLowerCase() + "%"));
            }

            // by min selling price
            if (Objects.nonNull(request.getMinSellingPrice())){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), request.getMinSellingPrice()));
            }

            // by max selling price
            if (Objects.nonNull(request.getMaxSellingPrice())){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), request.getMaxSellingPrice()));
            }

            // by purchase price
            if (Objects.nonNull(request.getPurchasePrice())){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("purchasePrice"), request.getPurchasePrice()));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(page, size);
        return productPriceRepository.findAll(specification, pageable);
//        return productPriceRepository.findAllByIsActiveTrue(specification, pageable);
    }
}
