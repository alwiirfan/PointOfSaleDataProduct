package com.pointofsale.dataSupplier.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateProductSupplierRequest {
    @NotBlank(message = "product name is required")
    @Size(min = 2, message = "must be 2 characters input to product name")
    private String productName;

    @NotBlank(message = "category is required")
    @Size(min = 2, message = "must be 2 characters input to category")
    private String category;

    @NotNull(message = "unit price is required")
    @Min(value = 0, message = "unit price must be greater than or equal to 0")
    private BigDecimal unitPrice;

    @NotNull(message = "total item is required")
    @Min(value = 2, message = "total item must be greater than or equal to 2")
    private Integer totalItem;

    @NotBlank(message = "merk is required")
    @Size(min = 2, message = "must be 2 characters input to merk")
    private String merk;

}
