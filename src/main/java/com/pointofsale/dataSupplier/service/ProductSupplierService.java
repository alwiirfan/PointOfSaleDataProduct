package com.pointofsale.dataSupplier.service;

import com.pointofsale.dataSupplier.dto.request.NewProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.response.ProductSupplierResponse;
import org.springframework.data.domain.Page;

public interface ProductSupplierService {
    ProductSupplierResponse create(NewProductSupplierRequest request);
    Page<ProductSupplierResponse> getAll(SearchProductSupplierRequest request, Integer page, Integer size);
    ProductSupplierResponse getById(String id);
    ProductSupplierResponse update(UpdateProductSupplierRequest request, String id);
    void delete(String id);
}
