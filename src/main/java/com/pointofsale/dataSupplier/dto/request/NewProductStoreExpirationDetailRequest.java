package com.pointofsale.dataSupplier.dto.request;

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
public class NewProductStoreExpirationDetailRequest {

    @NotBlank(message = "product store code is required")
    private String productStoreCode;

    @NotBlank(message = "product name is required")
    private String productStoreExpirationName;

    @NotBlank(message = "description is required")
    private String productStoreExpirationDescription;

    @NotBlank(message = "merk is required")
    private String productStoreExpirationMerk;
    
    @NotNull(message = "stock is required")
    @Min(value = 1, message = "stock must be greater than or equal to 1")
    private Integer productStoreExpirationTotalItem;
}
