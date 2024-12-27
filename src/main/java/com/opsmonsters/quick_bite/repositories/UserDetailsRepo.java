package com.opsmonsters.quick_bite.repositories;

import com.opsmonsters.quick_bite.models.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepo extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByEmail(String email);

}

