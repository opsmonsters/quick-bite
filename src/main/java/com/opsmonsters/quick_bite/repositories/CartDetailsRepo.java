package com.opsmonsters.quick_bite.repositories;

import com.opsmonsters.quick_bite.models.Cart;
import com.opsmonsters.quick_bite.models.CartDetails;
import com.opsmonsters.quick_bite.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartDetailsRepo extends JpaRepository<CartDetails, Long> {

    Optional<CartDetails> findByCartAndProduct(Cart cart, Product product);

    List<CartDetails> findByCart(Cart cart);

    void deleteAllByCart(Cart cart);
}