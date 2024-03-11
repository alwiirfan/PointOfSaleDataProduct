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
public class ProductSupplierResponse {
    private String productSupplierId;
    private String productName;
    private String category;
    private BigDecimal unitPrice;
    private Integer totalItem;
    private BigDecimal totalPrice;
    private String merk;
}
