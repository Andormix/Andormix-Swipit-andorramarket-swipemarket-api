package com.andormix.swipemarketapi.interaction;

import com.andormix.swipemarketapi.product.Product;
import com.andormix.swipemarketapi.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

/* Nota de Eric: He usado ingeniería inversa (por probar y ver dif) pàra generar esta entidad,
  limpiado y añadido metodos - He dejado en forma de comentarios los cambos para estudio propio
 */

@Entity
@Table(name = "product_swipes")
public class ProductSwipe
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    //OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // @Size(max = 20) Como es enum no es necesario.
    // @NotNull No hace falta ya que tiene @Column(nullable = false)
    // @Column(name = "action", nullable = false, length = 20)
    // private String action; La autogeneración no sabe que uso el enum

    @Column( nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SwipeAction action;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // No genera el constructor automaticamente
    public ProductSwipe(User user, Product product, SwipeAction action)
    {
        this.user = user;
        this.product = product;
        this.action = action;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    protected ProductSwipe() {
        // Constructor vacío exigido por JPA tampoco creado auto.
    }

    public void changeAction(SwipeAction action) {
        this.action = action;
        this.updatedAt = Instant.now();
    }

    // Genera auto los getters y setter de todo (ELIMINAR)

    /*
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
    }*/

    //Mod
    public SwipeAction getAction() {
        return action;
    }

    /*
    public void setAction(SwipeAction action) {
        this.action = action;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }*/

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /*
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }*/

    //New ones

    public Long getProductId() {
        return product.getId();
    }


}