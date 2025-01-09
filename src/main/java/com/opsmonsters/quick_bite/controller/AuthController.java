package com.opsmonsters.quick_bite.controller;

import com.opsmonsters.quick_bite.dto.ForgotPasswordDto;
import com.opsmonsters.quick_bite.dto.ResetPasswordDto;
import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserServices userServices;

    @PostMapping("/forgot-password")
    public ResponseDto forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        return userServices.forgotPassword(forgotPasswordDto);
    }

    @PostMapping("/resetPassword")
    public ResponseDto resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {

        String token = resetPasswordDto.getToken();
        String newPassword = resetPasswordDto.getNewPassword();

        return userServices.resetPassword(token, newPassword);
    }
}
