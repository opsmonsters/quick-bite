package com.opsmonsters.quick_bite.controller;

import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.dto.UserDto;
import com.opsmonsters.quick_bite.models.Users;
import com.opsmonsters.quick_bite.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserServices userService;


    @PostMapping
    public ResponseEntity<ResponseDto> createUser(@RequestBody Users user) {
        ResponseDto response = userService.createUser(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = (List<UserDto>) userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto> getUserById(@PathVariable Long userId) {
        ResponseDto response = userService.getUserById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseDto> updateUser(@PathVariable Long userId, @RequestBody Users dto) {
        ResponseDto response = userService.updateUser(userId, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable Long userId) {
        ResponseDto response = userService.deleteUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

