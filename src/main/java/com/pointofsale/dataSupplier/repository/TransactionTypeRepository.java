package com.pointofsale.dataSupplier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pointofsale.dataSupplier.constant.ETransactionType;
import com.pointofsale.dataSupplier.entity.TransactionType;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, String> {
    Optional<TransactionType> findFirstByTransactionType(ETransactionType transactionType);
}
