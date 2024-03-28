package com.pointofsale.dataSupplier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductStoreExpirationResponse {
    private String productStoreExpirationId;
    private String productCode;
    private String productName;
    private String description;
    private String category;
    private String merk;
    private Integer stock;
    private String createdAt;
    private String updatedAt;
    private ProductStoreResponse productStore;
}
