package com.pointofsale.dataSupplier.service;

import org.springframework.data.domain.Page;

import com.pointofsale.dataSupplier.dto.request.NewProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.response.ProductStoreExpirationResponse;

public interface ProductStoreExpirationService {
    ProductStoreExpirationResponse create(NewProductStoreExpirationRequest request);
    Page<ProductStoreExpirationResponse> getAll(SearchProductStoreExpirationRequest request, Integer page, Integer size);
    void delete(String id);
}
