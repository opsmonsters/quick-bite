package com.opsmonsters.quick_bite.controller;

import com.opsmonsters.quick_bite.dto.ForgotPasswordDto;
import com.opsmonsters.quick_bite.dto.LoginDto;
import com.opsmonsters.quick_bite.dto.ResetPasswordDto;
import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.services.AuthServices;
import com.opsmonsters.quick_bite.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthServices authServices;

    @Autowired
    private UserService userService;

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
