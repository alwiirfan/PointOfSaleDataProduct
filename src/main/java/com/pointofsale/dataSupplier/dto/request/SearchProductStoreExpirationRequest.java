package com.pointofsale.dataSupplier.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SearchProductStoreExpirationRequest {
    private String category;
    private String productExpirationDetailCode;
    private String productExpirationDetailName;
    private String productExpirationDetailMerk;
    private Integer productExpirationDetailMinTotalItem;
    private Integer productExpirationDetailMaxTotalItem;
    private String startDate;
    private String endDate;
}
