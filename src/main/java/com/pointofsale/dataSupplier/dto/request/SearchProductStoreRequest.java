package com.pointofsale.dataSupplier.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SearchProductStoreRequest {
    private String productCode;
    private String productName;
    private String Category;
    private String merk;
    private Integer purchasePrice;
    private Integer minSellingPrice;
    private Integer maxSellingPrice;
}
