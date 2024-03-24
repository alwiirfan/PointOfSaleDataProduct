package com.pointofsale.dataSupplier.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SearchSalesHistoryRequest {
    private String startDate;
    private String endDate;
}
