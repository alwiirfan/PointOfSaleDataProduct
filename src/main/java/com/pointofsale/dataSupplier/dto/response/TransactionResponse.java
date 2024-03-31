package com.pointofsale.dataSupplier.dto.response;

import java.util.List;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private String transactionId;
    private String transactionDate;
    private String transactionType;
    private String noStruk;
    private BigDecimal payment;
    private BigDecimal back;
    private BigDecimal totalPrice;
    private List<TransactionDetailResponse> transactionDetails;
    private String createdAt;
    private String updatedAt;
}
