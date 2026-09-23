package com.andormix.swipemarketapi.interaction;

import com.andormix.swipemarketapi.product.Product;
import com.andormix.swipemarketapi.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductSwipeRepository extends JpaRepository<ProductSwipe, Long> {

    Optional<ProductSwipe> findByUserAndProduct(
            User user,
            Product product
    );

    List<ProductSwipe> findByUserAndAction(
            User user,
            SwipeAction action
    );
}

/*
* public interface ProductRepository extends JpaRepository<Product, Long>,  JpaSpecificationExecutor<Product> {

    // Legacy, no lo necesitamos mas.
    //Page<Product> findByStatus(ProductStatus status, Pageable pageable);
}
* */