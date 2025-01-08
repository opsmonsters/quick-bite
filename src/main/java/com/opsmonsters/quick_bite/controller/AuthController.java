package com.opsmonsters.quick_bite.controller;

import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.models.LoginDto;
import com.opsmonsters.quick_bite.services.AuthServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class AuthController {

    @Autowired
    AuthServices authService;

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto dto){
        ResponseDto response = authService.userLogin(dto);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }
}

