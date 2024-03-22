package com.pointofsale.dataSupplier.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products_supplier")
@Builder(toBuilder = true)
public class ProductSupplier extends BaseEntity {

    @Column(name = "product_name", length = 300, nullable = false)
    private String productName;

    @Column(name = "unit_price", nullable = false, columnDefinition = "decimal(19,2) check (unit_price > 0)")
    private BigDecimal unitPrice;

    @Column(name = "total_item", nullable = false, columnDefinition = "int check (total_item > 0)")
    private Integer totalItem;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "total_price", nullable = false, columnDefinition = "decimal(19,2) check (total_price > 0)")
    private BigDecimal totalPrice;

    @Column(name = "merk", length = 150, nullable = false, columnDefinition = "varchar(150) check (merk <> '')")
    private String merk;
}
