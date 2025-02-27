package com.opsmonsters.quick_bite.repositories;

import com.opsmonsters.quick_bite.models.Cart;
import com.opsmonsters.quick_bite.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {

    Optional<Cart> findFirstByUserAndStatus(Users user, String status);
    List<Cart> findByUser(Users user);
    List<Cart> findAllByUserAndStatus(Users user, String status);
}