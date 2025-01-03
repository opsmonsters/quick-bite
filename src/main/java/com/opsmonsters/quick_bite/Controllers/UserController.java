package com.opsmonsters.quick_bite.Controllers;

import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.Services.UserServices;
import com.opsmonsters.quick_bite.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserServices userServices;

    @GetMapping("/signup")
    public ResponseEntity<List<Users>> getAllUsers() {
        ResponseDto response = userServices.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body((List<Users>) response.getData());
    }

    @GetMapping("/signup/{id}")
    public ResponseEntity<ResponseDto> getUserById(@PathVariable Long id) {
        ResponseDto response = userServices.getUserById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> createUser(@RequestBody Users user) {
        ResponseDto response = userServices.createUser(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateUser(@PathVariable Long id, @RequestBody Users user) {
        ResponseDto response = userServices.updateUser(id, user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable Long id) {
        ResponseDto response = userServices.deleteUser(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/email")
    public ResponseEntity<Users> getUserByEmail(@RequestParam String email) {
        Users response = userServices.getUserByEmail(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
