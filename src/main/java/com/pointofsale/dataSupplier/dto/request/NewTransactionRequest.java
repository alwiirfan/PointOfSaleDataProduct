package com.pointofsale.dataSupplier.dto.request;

import java.util.List;
import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NewTransactionRequest {

    @NotBlank(message = "transaction type is required")
    private String transactionType;

    @NotNull(message = "payment is required")
    @Min(value = 0, message = "payment must be greater than or equal to 0")
    private BigDecimal payment;
    
    private List<NewTransactionDetailRequest> transactionDetails;
}
