package com.pointofsale.dataSupplier.entity;

import java.math.BigDecimal;

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
@Builder(toBuilder = true)
@Table(name = "transaction_details")
public class TransactionDetail extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "product_store_id")
    @JsonBackReference
    private ProductStore productStore;

    @Column(name = "total_item", nullable = false)
    private Integer totalItem;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;
    
}
