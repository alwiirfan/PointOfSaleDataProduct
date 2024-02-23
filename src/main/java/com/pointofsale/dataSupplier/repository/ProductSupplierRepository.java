package com.pointofsale.dataSupplier.repository;

import com.pointofsale.dataSupplier.entity.ProductSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, String>, JpaSpecificationExecutor<ProductSupplier> {

}
