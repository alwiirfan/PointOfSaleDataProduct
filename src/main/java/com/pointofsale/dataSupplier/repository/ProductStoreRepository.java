package com.pointofsale.dataSupplier.repository;

import com.pointofsale.dataSupplier.entity.ProductStore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductStoreRepository extends JpaRepository<ProductStore, String>, JpaSpecificationExecutor<ProductStore> {
    Optional<ProductStore> findFirstByProductCode(String code);
}
