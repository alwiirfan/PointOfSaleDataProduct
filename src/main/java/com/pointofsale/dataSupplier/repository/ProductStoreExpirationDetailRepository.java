package com.pointofsale.dataSupplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pointofsale.dataSupplier.entity.ProductStoreExpirationDetail;

@Repository
public interface ProductStoreExpirationDetailRepository extends JpaRepository<ProductStoreExpirationDetail, String> {
}
