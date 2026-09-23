package com.andormix.swipemarketapi.interaction;

import com.andormix.swipemarketapi.product.Product;
import com.andormix.swipemarketapi.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface ProductFavoriteRepository extends JpaRepository<ProductFavorite, Long> {

    boolean existsByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);
    List<ProductFavorite> findByUser(User user);

    // TODO: Podriamos devolver listas en vez de un chunk en lista o pasar IDs sin usar clases.
    Page<ProductFavorite> findByUserId(Long userId, Pageable pageable);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserIdAndProductId(Long userId, Long productId);
}