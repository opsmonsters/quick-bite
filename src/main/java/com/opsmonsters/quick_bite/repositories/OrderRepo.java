package com.opsmonsters.quick_bite.repositories;

import com.opsmonsters.quick_bite.models.Order;
import com.opsmonsters.quick_bite.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {


    List<Order> findByUser_UserId(Long userId);
    List<Order> findByUser(Users user);
}
