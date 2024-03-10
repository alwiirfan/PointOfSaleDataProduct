package com.pointofsale.dataSupplier.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NewCategoryRequest {

    @NotBlank(message = "Category cannot be blank")
    private String category;
}
