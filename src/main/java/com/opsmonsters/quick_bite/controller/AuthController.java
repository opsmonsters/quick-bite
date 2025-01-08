package com.opsmonsters.quick_bite.controller;

import com.opsmonsters.quick_bite.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class AuthController {

    @Autowired
    AuthServices authServices;
    private UserServices userServices;

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto loginDto) {
    @PostMapping("/forgot-password")
    public ResponseDto forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        return userServices.forgotPassword(forgotPasswordDto);
    }

        ResponseDto response = authServices.userLogin(loginDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    @PostMapping("/reset-password")
    public ResponseDto resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return userServices.resetPassword(resetPasswordDto);
    }
}
}
