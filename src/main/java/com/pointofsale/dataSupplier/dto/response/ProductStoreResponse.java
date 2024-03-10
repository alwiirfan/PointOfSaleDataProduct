package com.pointofsale.dataSupplier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductStoreResponse {
    private String productStoreId;
    private String productCode;
    private String productName;
    private String productCategory;
    private String productDescription;
    private BigDecimal productPurchasePrice;
    private BigDecimal productSellingPrice;
    private Integer productStock;
    private String productMerk;

}
