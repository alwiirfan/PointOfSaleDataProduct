package com.pointofsale.dataSupplier.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NewTransactionDetailRequest {

    @NotBlank(message = "product code is required")
    @Size(min = 2, message = "must be 2 characters input to product code")
    private String productCode;

    @NotNull(message = "total item is required")
    @Min(value = 1, message = "total item must be greater than or equal to 1")
    private Integer totalItem;
}
