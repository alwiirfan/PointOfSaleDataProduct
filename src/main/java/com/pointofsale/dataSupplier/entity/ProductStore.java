package com.pointofsale.dataSupplier.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_store")
@Builder(toBuilder = true)
public class ProductStore extends BaseEntity {

    @Column(name = "product_code", length = 6 ,unique = true)
    private String productCode;

    @Column(name = "product_name", length = 300, nullable = false)
    private String productName;

    @Column(length = 500, nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "productStore", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<ProductPrice> productPrices;

    @Column(name = "merk", length = 200)
    private String merk;

    public void addProductPrice(ProductPrice productPrice) {
        productPrices.add(productPrice);
    }
}
