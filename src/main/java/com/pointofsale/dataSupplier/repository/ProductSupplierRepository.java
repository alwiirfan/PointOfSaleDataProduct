package com.pointofsale.dataSupplier.repository;

import com.pointofsale.dataSupplier.entity.ProductSupplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, String>, JpaSpecificationExecutor<ProductSupplier> {

    @Query("SELECT ps FROM ProductSupplier ps JOIN ps.category c WHERE c.category = :category")
    Page<ProductSupplier> findAllProductSupplierByCategory(@Param("category") String category, Pageable pageable);
}
