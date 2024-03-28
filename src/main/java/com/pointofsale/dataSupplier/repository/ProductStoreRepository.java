package com.pointofsale.dataSupplier.repository;

import com.pointofsale.dataSupplier.entity.Category;
import com.pointofsale.dataSupplier.entity.ProductStore;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStoreRepository extends JpaRepository<ProductStore, String>, JpaSpecificationExecutor<ProductStore> {
    Optional<ProductStore> findFirstByProductCode(String code);
    Optional<ProductStore> findFirstByProductPrice_IsActiveTrueAndId(String id);
    Optional<ProductStore> findFirstByProductPrice_IsActiveTrueAndProductCode(String productStoreCode);
    List<ProductStore> findAllByCategory(Category category);
    Page<ProductStore> findAllByProductPrice_IsActiveTrue(Specification<ProductStore> specification, Pageable pageable);
}
