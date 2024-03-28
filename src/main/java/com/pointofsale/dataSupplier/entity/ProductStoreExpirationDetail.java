package com.pointofsale.dataSupplier.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "products_store_expiration_detail")
@Builder(toBuilder = true)
public class ProductStoreExpirationDetail extends BaseEntity {
    
    @Column(name = "merk", length = 200)
    private String merk;

    @Column(name = "total_item", nullable = false)
    private Integer totalItem;

    @Column(name = "product_code", length = 7 ,unique = true)
    private String productCode;

    @Column(name = "product_name", length = 300, nullable = false)
    private String productName;

    @Column(length = 500, nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "product_store_id")
    @JsonBackReference
    private ProductStore productStore;

    @ManyToOne
    @JoinColumn(name = "product_store_expiration_id")
    private ProductStoreExpiration productStoreExpiration;
}
