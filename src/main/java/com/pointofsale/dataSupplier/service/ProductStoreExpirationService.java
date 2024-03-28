package com.pointofsale.dataSupplier.service;

import org.springframework.data.domain.Page;

import com.pointofsale.dataSupplier.dto.request.NewProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.response.ProductStoreExpirationResponse;
import com.pointofsale.dataSupplier.entity.ProductStoreExpiration;

public interface ProductStoreExpirationService {
    ProductStoreExpirationResponse create(NewProductStoreExpirationRequest request, String productStoreCode);
    Page<ProductStoreExpirationResponse> getAll(SearchProductStoreExpirationRequest request, Integer page, Integer size);
    ProductStoreExpiration update(UpdateProductStoreExpirationRequest request, 
                                                            String productStoreCode, String productStoreExpirationCode);
    void delete(String id);

    ProductStoreExpiration getByProductStoreExpirationCode(String productStoreExpirationCode);
}
