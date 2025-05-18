package com.Electro.Responsitory;

import com.Electro.Entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findById(Long cartId);
    Optional<Cart> findByUserId(Long userId);
}
