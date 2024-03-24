package com.pointofsale.dataSupplier.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SearchTransactionRequest {
    private String startDate;
    private String endDate;
    private String transactionType;
    private String productName;
    private String productCode;
}
