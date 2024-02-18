package com.pointofsale.dataSupplier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_supplier")
@Builder(toBuilder = true)
public class ProductSupplier extends BaseEntity {

    private String productName;

    private BigDecimal unitPrice;

    private Integer totalItem;

    private BigDecimal totalPrice;
}
