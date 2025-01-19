package com.opsmonsters.quick_bite.controller;
import com.opsmonsters.quick_bite.dto.*;
import com.opsmonsters.quick_bite.services.AuthServices;
import com.opsmonsters.quick_bite.services.OtpService;
import com.opsmonsters.quick_bite.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class AuthController {
    @Autowired
    private AuthServices authServices;
    @Autowired
    private OtpService otpService;
    @Autowired
    private UserService userService;
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
}
