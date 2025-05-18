package com.Electro.Entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARTID")
    private Long CartId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart",cascade = CascadeType.ALL)
    //@JoinColumn(name = "cart_id") // Tên cột khóa ngoại trong bảng CartDetails
    private List<CartDetails> cartDetails;


    public void updateTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartDetails cartDetail : cartDetails) {
            totalPrice = totalPrice.add(cartDetail.getProduct().getProductPrice().multiply(BigDecimal.valueOf(cartDetail.getQuantity())));
        }
        this.total = totalPrice;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<CartDetails> getCartDetails() {
        return cartDetails;
    }

    public Long getCartId() {
        return CartId;
    }

    public void setCartId(Long cartId) {
        CartId = cartId;
    }

    public void setCartDetails(List<CartDetails> cartDetails) {
        this.cartDetails = cartDetails;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private BigDecimal total; // Tổng tiền trong giỏ hàng


    public Cart() {
    }

    public Cart(Long id) {
        this.CartId = id;

    }

    public Long getId() {
        return CartId;
    }

    public void setId(Long id) {
        this.CartId = id;
    }



    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
