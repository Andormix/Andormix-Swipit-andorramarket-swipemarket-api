package com.andormix.swipemarketapi.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>,  JpaSpecificationExecutor<Product> {

    // Legacy, no lo necesitamos mas.
    //Page<Product> findByStatus(ProductStatus status, Pageable pageable);
}

/*  JpaSpecificationExecutor<Product> el spec será ProductSpecifications

    findAll(Specification<T> spec, Pageable pageable)
    findOne(Specification<T> spec)
    count(Specification<T> spec)
* */