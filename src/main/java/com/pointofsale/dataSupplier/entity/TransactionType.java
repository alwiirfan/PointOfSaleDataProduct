package com.pointofsale.dataSupplier.entity;

import com.pointofsale.dataSupplier.constant.ETransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "transaction_types")
public class TransactionType extends BaseEntity {
    
    @Enumerated(value = EnumType.STRING)
    private ETransactionType transactionType;

}
