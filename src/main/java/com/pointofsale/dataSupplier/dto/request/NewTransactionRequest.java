package com.pointofsale.dataSupplier.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
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
    
    private List<NewTransactionDetailRequest> transactionDetails;
}
