package com.opsmonsters.quick_bite.repositories;

import com.opsmonsters.quick_bite.models.Otp;
import com.opsmonsters.quick_bite.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepo extends JpaRepository<Otp, Long> {


    Optional<Otp> findByUserAndOtp(Users user, String otp);


    Optional<Otp> findTopByUserOrderByCreatedAtDesc(Users user);


    void deleteByUser(Users user);

}