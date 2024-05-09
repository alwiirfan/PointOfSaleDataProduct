package com.pointofsale.dataSupplier.entity;

import com.pointofsale.dataSupplier.entity.embeddable.Address;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
@Table(name = "stores")
@Builder(toBuilder = true)
public class Store extends BaseEntity {
    
    @Column(name = "store_name", length = 100, nullable = false, unique = true)
    private String storeName;

    @Column(name = "store_description", length = 200, nullable = false)
    private String storeDescription;

    @Embedded
    private Address address;

}
