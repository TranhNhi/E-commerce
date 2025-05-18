package com.Electro.Service;

import com.Electro.Entities.Cart;
import com.Electro.Entities.CartDetails;

import java.util.List;

public interface CartService {
    public void createInvoiceFromCart(Long cartId);
    public void addProductToCart(Long cartId, Long productId, int quantity);
    public Cart getCartById(Long cartId);
    public void removeProductFromCart(Long cartId, Long productId);
    public void increaseCartItemQuantity(Long cartId, Long productId);
    public void decreaseCartItemQuantity(Long cartId, Long productId);
    public List<CartDetails> getAllCartDetails(Long cartId);
}
