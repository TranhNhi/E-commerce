package com.Electro.Controller;

import com.Electro.Entities.CartDetails;
import com.Electro.Service.CartService;
import com.Electro.Service.ProductService;
import com.Electro.payload.request.CartRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }


    @PostMapping("/{cartId}/addProduct")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addProductToCart(@PathVariable Long cartId,
                                              @RequestBody CartRequest request) {
        cartService.addProductToCart(cartId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok("Product added to cart successfully.");
    }

    @DeleteMapping("/{cartId}/products/{productId}")  // Xóa sản phẩm khỏi giỏ hàng
    public ResponseEntity<?> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        cartService.removeProductFromCart(cartId, productId);
        return ResponseEntity.ok("Product removed from cart");
    }

    @GetMapping("/{cartId}/products")        // Lấy thông tin giỏ hàng
    public ResponseEntity<List<CartDetails>> getCart(@PathVariable Long cartId) {
        List<CartDetails> cartDetails = cartService.getAllCartDetails(cartId);
        return ResponseEntity.ok(cartDetails);
    }


    @PostMapping("/{cartId}/products/{productId}/increase")         // Tăng số lượng sản phẩm
    public ResponseEntity<?> increaseCartItemQuantity(@PathVariable Long cartId, @PathVariable Long productId) {
        cartService.increaseCartItemQuantity(cartId, productId);
        return ResponseEntity.ok("Cart item quantity increased");
    }

    @PostMapping("/{cartId}/products/{productId}/decrease")     // Giảm số lượng sản phẩm
    public ResponseEntity<?> decreaseCartItemQuantity(@PathVariable Long cartId, @PathVariable Long productId) {
        cartService.decreaseCartItemQuantity(cartId, productId);
        return ResponseEntity.ok("Cart item quantity decreased");
    }

    // Xuất hóa đơn
    @PostMapping("/{cartId}/checkout")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> checkout(@PathVariable Long cartId) {
        cartService.createInvoiceFromCart(cartId);
        //cartService.clearCart(cartId); // Xóa tất cả sản phẩm đã có trong giỏ hàng sau khi thanh toán
        return ResponseEntity.ok("Checkout successful");
    }
}
