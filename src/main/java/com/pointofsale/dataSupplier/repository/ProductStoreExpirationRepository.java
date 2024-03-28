package com.pointofsale.dataSupplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pointofsale.dataSupplier.entity.ProductStoreExpiration;

@Repository
public interface ProductStoreExpirationRepository extends JpaRepository<ProductStoreExpiration, String>, JpaSpecificationExecutor<ProductStoreExpiration> {
}
