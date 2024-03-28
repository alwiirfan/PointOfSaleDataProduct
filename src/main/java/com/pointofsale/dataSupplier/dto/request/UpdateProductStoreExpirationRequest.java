package com.pointofsale.dataSupplier.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateProductStoreExpirationRequest {
    private String productName;
    private String description;
    private String category;
    private String merk;
    private Integer stock;
}
