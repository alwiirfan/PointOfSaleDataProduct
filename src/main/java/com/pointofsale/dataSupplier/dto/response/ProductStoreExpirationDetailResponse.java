package com.pointofsale.dataSupplier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductStoreExpirationDetailResponse {
    private String productStoreExpirationDetailId;
    private String productStoreExpirationCode;
    private String productStoreExpirationName;
    private String productStoreExpirationDescription;
    private String productStoreExpirationMerk;
    private Integer productStoreExpirationTotalItem;
    private ProductStoreResponse productStore;
    private String createdAt;
    private String updatedAt;
}
