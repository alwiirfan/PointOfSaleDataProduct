package com.pointofsale.dataSupplier.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NewProductStoreExpirationRequest {

    @NotBlank(message = "category is required")
    private String category;

    List<NewProductStoreExpirationDetailRequest> productStoreExpirationDetails;
}
