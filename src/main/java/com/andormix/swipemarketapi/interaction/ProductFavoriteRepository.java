package com.andormix.swipemarketapi.interaction;

import com.andormix.swipemarketapi.product.Product;
import com.andormix.swipemarketapi.product.ProductCategory;
import com.andormix.swipemarketapi.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProductFavoriteRepository extends JpaRepository<ProductFavorite, Long> {

    boolean existsByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);
    List<ProductFavorite> findByUser(User user);

    // TODO: Podriamos devolver listas en vez de un chunk en lista o pasar IDs sin usar clases.
    Page<ProductFavorite> findByUserId(Long userId, Pageable pageable);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserIdAndProductId(Long userId, Long productId);

    // Features extra
    List<ProductFavorite> findByUserAndProductCategory(User user, ProductCategory category);


    //Testing @ QUERY
    @Query("""
        SELECT f FROM ProductFavorite f
        WHERE f.user = :user
          AND (:category IS NULL OR f.product.category = :category)
          AND (:search IS NULL OR LOWER(f.product.title) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    List<ProductFavorite> findFavoritesWithFilters(
            @Param("user") User user,
            @Param("category") ProductCategory category,
            @Param("search") String search
    );



}