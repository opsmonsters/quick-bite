package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.models.Otp;
import com.opsmonsters.quick_bite.repositories.OtpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;

@Service
public class OtpService{

    @Autowired
    private OtpRepo otpRepo;

    public ResponseDto generateOtp(String userId) {

        String otp = String.format("%06d", new SecureRandom().nextInt(999999));
        Otp otpEntity = new Otp();
        otpEntity.setUserId(userId);
        otpEntity.setOtp(otp);
        otpEntity.setCreatedAt(new Date());
        otpEntity.setExpiresAt(new Date(System.currentTimeMillis() + 300000));
        otpEntity.setIsUsed(false);
        otpRepo.save(otpEntity);



        return new ResponseDto(200, "OTP generated and sent successfully", null);
    }

    public ResponseDto validateOtp(String userId, String otp) {
        Otp otpEntity = otpRepo.findByUserIdAndOtp(userId, otp);

        if (otpEntity == null) {
            return new ResponseDto(400, "Invalid OTP", null);
        }

        if (otpEntity.getIsUsed()) {
            return new ResponseDto(400, "OTP already used", null);
        }
    }

        if (new Date().after(otpEntity.getExpiresAt())) {
            return new ResponseDto(400, "OTP expired", null);
        }

            return new ResponseDto(400, "Invalid or expired OTP.");
        } catch (Exception e) {
            return new ResponseDto(500, "Error while validating OTP: " + e.getMessage());
        }
    }

    public ResponseDto clearOtp(String userId) {
        try {

            otpRepo.deleteByUserId(userId);
            return new ResponseDto(200, "All OTPs for user " + userId + " have been cleared successfully.");
        } catch (Exception e) {
            return new ResponseDto(500, "An error occurred while clearing OTPs: " + e.getMessage());
        }
    }
}




