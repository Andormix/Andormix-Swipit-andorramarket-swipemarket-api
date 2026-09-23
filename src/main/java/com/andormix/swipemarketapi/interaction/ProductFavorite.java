package com.andormix.swipemarketapi.interaction;

import com.andormix.swipemarketapi.product.Product;
import com.andormix.swipemarketapi.user.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "product_favorites",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_product_favorites_user_product",
                columnNames = {"user_id", "product_id"}
        )
)
public class ProductFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ProductFavorite() {
    }

    public ProductFavorite(User user, Product product) {
        this.user = user;
        this.product = product;
        this.createdAt = Instant.now();
    }

    public Product getProduct() {
        return product;
    }
}