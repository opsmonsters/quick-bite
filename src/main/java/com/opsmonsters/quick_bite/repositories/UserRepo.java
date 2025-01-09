package com.opsmonsters.quick_bite.repositories;

import com.opsmonsters.quick_bite.models.Otp;
import com.opsmonsters.quick_bite.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {

    Optional<Otp> findByEmail(String email);

    Optional<Users> findByResetToken(String resetToken);
}
