package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.dto.OtpDto;
import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.models.Otp;
import com.opsmonsters.quick_bite.repositories.OtpRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepo otpRepo;

    private static final int OTP_VALIDITY = 5 * 60 * 1000; // 5 minutes

    public ResponseDto generateOtp(String userId) {
        try {

            String otpCode = String.format("%06d", new Random().nextInt(999999));


            Otp otp = new Otp();
            otp.setOtp(otpCode);
            otp.setUserId(userId);
            otp.setCreatedAt(new Date());
            otp.setExpiresAt(new Date(System.currentTimeMillis() + OTP_VALIDITY));
            otp.setIsUsed(false);

        return new ResponseDto(200, "OTP generated and sent successfully", null);
    }

            otpRepo.save(otp);


            OtpDto otpDto = new OtpDto();
            otpDto.setOtp(otp.getOtp());
            otpDto.setUserId(otp.getUserId());
            otpDto.setCreatedAt(otp.getCreatedAt());
            otpDto.setExpiresAt(otp.getExpiresAt());
            otpDto.setIsUsed(otp.getIsUsed());

            return new ResponseDto(201, "OTP generated successfully!", otpDto);
        } catch (Exception e) {
            return new ResponseDto(500, "Error while generating OTP: " + e.getMessage());
        }
    }

    public ResponseDto validateOtp(String userId, String otpCode) {
        try {

            Optional<Otp> otpOptional = otpRepo.findTopByUserIdOrderByCreatedAtDesc(userId);

            if (otpOptional.isPresent()) {
                Otp otp = otpOptional.get();


                if (!otp.getIsUsed() && otp.getOtp().equals(otpCode) && otp.getExpiresAt().after(new Date())) {

                    otp.setIsUsed(true);
                    otpRepo.save(otp);

                    return new ResponseDto(200, "OTP is valid.");
                }
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
