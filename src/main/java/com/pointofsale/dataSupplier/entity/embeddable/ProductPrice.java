package com.pointofsale.dataSupplier.entity.embeddable;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Embeddable
public class ProductPrice {
    @Column( name = "purchase_price", nullable = false, columnDefinition = "decimal(19,2) check (purchase_price > 0) not null")
    private BigDecimal purchasePrice;

    @Column(name = "selling_price", nullable = false, columnDefinition = "decimal(19,2) check (selling_price > 0) not null")
    private BigDecimal sellingPrice;

    @Column(nullable = false, columnDefinition = "int check (stock > 0)")
    private Integer stock;
}
