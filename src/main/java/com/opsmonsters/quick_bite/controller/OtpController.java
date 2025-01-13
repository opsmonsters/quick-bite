package com.opsmonsters.quick_bite.controller;
import com.opsmonsters.quick_bite.services.OtpService;
import com.opsmonsters.quick_bite.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/generate")
    public ResponseEntity<ResponseDto> generateOtp(@RequestParam String email) {
        ResponseDto response = otpService.generateOtp(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/validate")
    public ResponseEntity<ResponseDto> validateOtp(@RequestParam long userId, @RequestParam String otp) {
        ResponseDto response = otpService.validateOtp(userId, otp);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @DeleteMapping("/clear")
    public ResponseEntity<ResponseDto> clearOtp(@RequestParam long userId) {
        ResponseDto response = otpService.clearOtp(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}