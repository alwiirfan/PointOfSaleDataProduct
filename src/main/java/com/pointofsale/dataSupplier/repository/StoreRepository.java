package com.pointofsale.dataSupplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pointofsale.dataSupplier.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {
    
}
