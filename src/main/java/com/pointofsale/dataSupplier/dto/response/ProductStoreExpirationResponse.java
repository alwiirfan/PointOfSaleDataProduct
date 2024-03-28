package com.pointofsale.dataSupplier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductStoreExpirationResponse {
    private String productStoreExpirationId;
    private String category;
    private String createdAt;
    private String updatedAt;
    List<ProductStoreExpirationDetailResponse> productStoreExpirationDetails;
}
