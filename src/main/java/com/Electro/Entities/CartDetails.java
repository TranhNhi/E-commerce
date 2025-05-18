package com.Electro.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "CartDetails")
public class CartDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartDetailsID")
    private Long CartDetailsId;
    @ManyToOne
    @JoinColumn(name = "CartID", referencedColumnName = "CARTID")
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "ProductId", referencedColumnName = "id")
    private Product product;

    private int quantity;
    @lombok.Setter
    @lombok.Getter
    private BigDecimal total;

    public CartDetails() {
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CartDetails(Long cartDetailsId, Cart cart, Product product, int quantity, BigDecimal total) {
        CartDetailsId = cartDetailsId;
        this.quantity = quantity;
        this.total = total;
    }

    public Long getCartDetailsId() {
        return CartDetailsId;
    }

    public void setCartDetailsId(Long cartDetailsId) {
        CartDetailsId = cartDetailsId;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
