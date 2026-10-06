package com.andormix.swipemarketapi.reviews;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Optional<Double> findAverageRatingByProductId(@Param("productId") Long productId);

    @Query("""
        SELECT r FROM Review r
        WHERE r.product.id = :productId
        ORDER BY r.createdAt DESC
    """)
    List<Review> findRecentByProductId(@Param("productId") Long productId, Pageable pageable);

    boolean existsByProductIdAndUserId(Long productId, Long userId);

    Long countByProductId(Long productId);

}
