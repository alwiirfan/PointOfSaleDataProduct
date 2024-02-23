package com.pointofsale.dataSupplier.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SearchProductSupplierRequest {
    private String productName;
    private Integer minUnitPrice;
    private Integer maxUnitPrice;
    private Integer totalItem;
    private Integer minTotalPrice;
    private Integer maxTotalPrice;
}
