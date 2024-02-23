package com.pointofsale.dataSupplier.repository;

import com.pointofsale.dataSupplier.entity.ProductPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, String>, JpaSpecificationExecutor<ProductPrice> {
    Optional<ProductPrice> findFirstByIsActiveTrueAndProductStore_Id(String productStoreId);
    Optional<ProductPrice> findFirstByIsActiveTrueAndProductStore_ProductCode(String productStoreCode);
    List<ProductPrice> findAllByProductStore_ProductCode(String productStoreCode);
    Page<ProductPrice> findAllByIsActiveTrue(Specification<ProductPrice> specification, Pageable pageable);

}
