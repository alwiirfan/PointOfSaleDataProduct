package com.pointofsale.dataSupplier.service;

import com.pointofsale.dataSupplier.dto.request.SearchProductStoreRequest;
import com.pointofsale.dataSupplier.entity.ProductPrice;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductPriceService {
    List<ProductPrice> getAllProductPriceByProductStoreCode(String productStoreCode);
    ProductPrice getActivePriceByProductStoreId(String productStoreId);
    ProductPrice getProductPriceById(String productPriceId);
    ProductPrice getActivePriceByProductStoreCode(String productStoreCode);
    Page<ProductPrice> getAllActiveProductPrice(SearchProductStoreRequest request, Integer page, Integer size);
}
