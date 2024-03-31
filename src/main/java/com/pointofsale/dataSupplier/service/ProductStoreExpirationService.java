package com.pointofsale.dataSupplier.service;

import org.springframework.data.domain.Page;

import com.pointofsale.dataSupplier.dto.request.NewProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.response.ProductStoreExpirationResponse;

public interface ProductStoreExpirationService {
    ProductStoreExpirationResponse create(NewProductStoreExpirationRequest request);
    ProductStoreExpirationResponse getById(String id);
    Page<ProductStoreExpirationResponse> getAll(SearchProductStoreExpirationRequest request, Integer page, Integer size);
}
