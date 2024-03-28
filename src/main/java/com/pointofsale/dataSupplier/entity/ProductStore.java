package com.pointofsale.dataSupplier.entity;

import com.pointofsale.dataSupplier.entity.embeddable.ProductPrice;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products_store")
@Builder(toBuilder = true)
public class ProductStore extends BaseEntity {

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

    @Embedded
    private ProductPrice productPrice;

    @OneToOne(mappedBy = "productStore", cascade = CascadeType.PERSIST)
    private ProductStoreExpiration productStoreExpiration;
}
