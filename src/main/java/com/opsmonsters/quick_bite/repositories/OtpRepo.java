package com.opsmonsters.quick_bite.repositories;

import com.opsmonsters.quick_bite.models.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepo extends JpaRepository<Otp, Long> {
    Otp findByUserIdAndOtp(String userId, String otp);
    void deleteByUserId(String userId);
    Optional<Otp> findTopByUserIdOrderByCreatedAtDesc(String userId);

}