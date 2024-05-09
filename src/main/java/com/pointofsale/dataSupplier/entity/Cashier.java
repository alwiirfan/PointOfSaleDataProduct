package com.pointofsale.dataSupplier.entity;

import com.pointofsale.dataSupplier.entity.embeddable.Address;
import com.pointofsale.dataSupplier.entity.embeddable.CompleteName;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
@Table(name = "cashiers")
@Builder(toBuilder = true)
public class Cashier extends BaseEntity {
    
    @Embedded
    private CompleteName completeName;

    @Embedded
    private Address address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;

}
