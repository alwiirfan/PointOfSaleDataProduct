package com.pointofsale.dataSupplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pointofsale.dataSupplier.constant.ETransactionType;
import com.pointofsale.dataSupplier.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    @Query("SELECT td.totalPrice FROM Transaction t JOIN t.transactionDetails td " + 
           "WHERE t.transactionType.transactionType = :eTransactionType or t.transactionDate between :startDate and :endDate " + 
           "GROUP BY t.transactionType")
    BigDecimal calculateTransactionGroupByTransactionType(@Param("eTransactionType") ETransactionType eTransactionType,
                                                          @Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate);
}
