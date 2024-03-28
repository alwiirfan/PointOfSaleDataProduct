package com.pointofsale.dataSupplier.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetailResponse {
    private String transactionDetailId;
    private String transactionId;
    private ProductStoreResponse productStore;
    private Integer totalItem;
    private BigDecimal totalPrice;
    private String createdAt;
    private String updatedAt;
}
