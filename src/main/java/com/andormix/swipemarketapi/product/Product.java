package com.andormix.swipemarketapi.product;

import com.andormix.swipemarketapi.user.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_condition", nullable = false, length = 30)
    private ProductCondition condition;

    @Column(nullable = false, length = 50)
    private String parish;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProductStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Product() {
    }

    public Product(
            User seller,
            String title,
            String description,
            BigDecimal price,
            ProductCategory category,
            ProductCondition condition,
            String parish
    ) {
        this.seller = seller;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.condition = condition;
        this.parish = parish;
        this.status = ProductStatus.ACTIVE;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void update(
            String title,
            String description,
            BigDecimal price,
            ProductCategory category,
            ProductCondition condition,
            String parish
    ) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.condition = condition;
        this.parish = parish;
        this.updatedAt = Instant.now();
    }

    public void changeStatus(ProductStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public User getSeller() {
        return seller;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public ProductCondition getCondition() {
        return condition;
    }

    public String getParish() {
        return parish;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}