package com.pointofsale.dataSupplier.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NewProductStoreRequest {
    @NotBlank(message = "product code is required")
    @Size(min = 6, max = 6, message = "Product code must be exactly 6 characters long")
    @Pattern(regexp = "\\d+", message = "Product code must contain only digits")
    @Pattern(regexp = "\\S+", message = "Product code must not contain spaces")
    private String productCode;

    @NotBlank(message = "product name is required")
    private String productName;

    @NotBlank(message = "description is required")
    private String description;

    @NotBlank(message = "product category is required")
    private String productCategory;

    @NotNull(message = "purchase price is required")
    @Min(value = 0, message = "purchase price must be greater than or equal to 0")
    private BigDecimal purchasePrice;

    @NotNull(message = "selling price is required")
    @Min(value = 0, message = "selling price must be greater than or equal to 0")
    private BigDecimal sellingPrice;

    @NotNull(message = "stock is required")
    @Min(value = 0, message = "stock must be greater than or equal to 0")
    private Integer productStock;

    @NotBlank(message = "merk is required")
    private String merk;
}