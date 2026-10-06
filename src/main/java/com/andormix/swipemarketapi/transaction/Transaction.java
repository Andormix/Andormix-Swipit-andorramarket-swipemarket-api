package com.andormix.swipemarketapi.transaction;

import com.andormix.swipemarketapi.product.Product;
import com.andormix.swipemarketapi.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Size(max = 30)
    @NotNull
    @Column(name = "transaction_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Size(max = 30)
    @ColumnDefault("'COMPLETED'")
    @Column(name = "transaction_status", length = 30)
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_At", insertable = false, updatable = false)
    private Instant createdAt;

    protected Transaction() {
    }


    public Transaction(User user, Product product, TransactionType transactionType, String note, BigDecimal amount) {
        this.user = user;
        this.product = product;
        this.transactionType = transactionType;
        this.amount = amount;
        this.note = note;
        //this.createdAt = Instant.now(); Lo hacemos desde la BD
        this.transactionStatus = TransactionStatus.COMPLETED;  //Para que no pase como null
    }

    @Lob
    @Column(name = "note")
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}