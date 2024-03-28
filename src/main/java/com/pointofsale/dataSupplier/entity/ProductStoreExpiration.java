package com.pointofsale.dataSupplier.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products_store_expiration")
@Builder(toBuilder = true)
public class ProductStoreExpiration extends BaseEntity {
    
    @Column(name = "product_code", length = 7 ,unique = true)
    private String productCode;

    @Column(name = "product_name", length = 300, nullable = false)
    private String productName;

    @Column(length = 500, nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "merk", length = 200)
    private String merk;

    @Column(name = "stock")
    private Integer stock;

    @OneToOne
    @JoinColumn(name = "product_store_id", referencedColumnName = "id")
    private ProductStore productStore;
}
