package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.models.Otp;
import com.opsmonsters.quick_bite.repositories.OtpRepo;
import com.opsmonsters.quick_bite.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;

@Service
public class OtpService {

    @Autowired
    private OtpRepo otpRepo;
    @Autowired
    private UserRepo userRepo;

    public ResponseDto generateOtp(String email) {
        try {

            Long userId = userRepo.findUserIdByEmail(email);

            if (userId == null) {
                return new ResponseDto(400, "User not found with the provided email", null);
            }


            String otp = String.format("%06d", new SecureRandom().nextInt(999999));
            Otp otpEntity = new Otp();
            otpEntity.setUserId(userId);
            otpEntity.setOtp(otp);
            otpEntity.setCreatedAt(new Date());
            otpEntity.setExpiresAt(new Date(System.currentTimeMillis() + 300000)); // 5 mins
            otpEntity.setIsUsed(false);
            otpRepo.save(otpEntity);

            return new ResponseDto(200, "OTP generated successfully", null);

        } catch (Exception e) {
            return new ResponseDto(500, "Error generating OTP: " + e.getMessage(), null);
        }
    }

    public ResponseDto validateOtp(long userId, String otp) {
        try {

            Otp otpEntity = otpRepo.findByUserIdAndOtp(userId, otp);

            if (otpEntity == null) {
                return new ResponseDto(400, "Invalid OTP", null);
            }

            if (otpEntity.getIsUsed()) {
                return new ResponseDto(400, "OTP already used", null);
            }

            if (new Date().after(otpEntity.getExpiresAt())) {
                return new ResponseDto(400, "OTP expired", null);
            }


            otpEntity.setIsUsed(true);
            otpRepo.save(otpEntity);

            return new ResponseDto(200, "OTP validated successfully", null);

        } catch (Exception e) {
            return new ResponseDto(500, "Error while validating OTP: " + e.getMessage());
        }
    }

    public ResponseDto clearOtp(long userId) {
        try {

            otpRepo.deleteByUserId(userId);
            return new ResponseDto(200, "All OTPs for user " + userId + " have been cleared successfully.");
        } catch (Exception e) {
            return new ResponseDto(500, "An error occurred while clearing OTPs: " + e.getMessage());
        }
    }
}





