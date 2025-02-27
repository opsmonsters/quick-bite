package com.opsmonsters.quick_bite.controller;

import com.opsmonsters.quick_bite.dto.*;
import com.opsmonsters.quick_bite.services.AuthServices;
import com.opsmonsters.quick_bite.services.BlackListService;

import com.opsmonsters.quick_bite.services.OtpService;
import com.opsmonsters.quick_bite.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController {
    @Autowired
    private AuthServices authServices;
    @Autowired
    private OtpService otpService;
    @Autowired
    private UserService userService;
    @Autowired
    private BlackListService blacklistService;

    @PostMapping("/otp")
    public ResponseEntity<ResponseDto> createOtp(@RequestBody OtpDto otpDto) {
        ResponseDto response = otpService.generateOtp(otpDto.getEmail());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto loginDto) {
        ResponseDto response = authServices.userLogin(loginDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseDto forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        return authServices.forgotPassword(forgotPasswordDto);
    }

    @PostMapping("/reset-password")
    public ResponseDto resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return authServices.resetPassword(resetPasswordDto);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            blacklistService.blacklistToken(token);
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }

}
