package com.pointofsale.dataSupplier.dto.request;

import java.util.List;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateTransactionRequest {
    private String transactionType;
    private BigDecimal payment;
    private List<UpdateTransactionDetailRequest> transactionDetails;
}
