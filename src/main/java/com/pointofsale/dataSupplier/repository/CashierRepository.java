package com.pointofsale.dataSupplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pointofsale.dataSupplier.entity.Cashier;

@Repository
public interface CashierRepository extends JpaRepository<Cashier, String> {
    
}
