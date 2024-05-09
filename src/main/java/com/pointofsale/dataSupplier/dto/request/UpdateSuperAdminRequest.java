package com.pointofsale.dataSupplier.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateSuperAdminRequest {
    
    @NotBlank(message = "full name is required")
    private String fullName;

    @NotBlank(message = "call name is required")
    private String callName;

    @NotBlank(message = "street is required")
    private String street;

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "province is required")
    private String province;

    @NotBlank(message = "country is required")
    private String country;
    
    @NotBlank(message = "date of birth is required")
    @Pattern(regexp = "^(\\d{2})-(\\d{2})-(\\d{4})$", message = "date of birth must be in the format dd-MM-yyyy")
    private String dateOfBirth;
    
}
