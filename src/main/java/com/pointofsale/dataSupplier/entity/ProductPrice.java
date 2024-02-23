package com.pointofsale.dataSupplier.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_price")
@Builder(toBuilder = true)
public class ProductPrice extends BaseEntity {
    @Column( name = "purchase_price", columnDefinition = "decimal(19,2) check (purchase_price > 0) not null")
    private BigDecimal purchasePrice;

    @Column(name = "selling_price", columnDefinition = "decimal(19,2) check (selling_price > 0) not null")
    private BigDecimal sellingPrice;

    @Column(columnDefinition = "int check (stock > 0)")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "product_Store_id")
    private ProductStore productStore;

    @Column(name = "is_active")
    private Boolean isActive;
}
