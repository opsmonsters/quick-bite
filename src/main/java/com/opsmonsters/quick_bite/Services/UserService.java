package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.dto.UserDto;
import com.opsmonsters.quick_bite.models.Users;
import com.opsmonsters.quick_bite.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public ResponseDto createUser(UserDto dto) {
        try {
            Optional<Users> existingUser = userRepo.findByEmail(dto.getEmail());
            if (existingUser.isPresent()) {
                return new ResponseDto(400, "User with email " + dto.getEmail() + " already exists!");
            }
            Users user = new Users();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setProfileImageUrl(dto.getProfileImageUrl());
            userRepo.save(user);

            return new ResponseDto(201, "User created successfully!");
        } catch (Exception e) {
            return new ResponseDto(500, "Error while creating user: " + e.getMessage());
        }
    }

    public List<UserDto> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setUserId(user.getUserId());
                    dto.setFirstName(user.getFirstName());
                    dto.setLastName(user.getLastName());
                    dto.setEmail(user.getEmail());
                    dto.setPhoneNumber(user.getPhoneNumber());
                    dto.setProfileImageUrl(user.getProfileImageUrl());
                    dto.setCreatedAt(user.getCreatedAt());
                    dto.setUpdatedAt(user.getUpdatedAt());
                    return dto;
                }).collect(Collectors.toList());
    }

    public ResponseDto getUserById(Long userId) {
        Optional<Users> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            return new ResponseDto(200, user);
        } else {
            return new ResponseDto(404, "User with ID " + userId + " not found.");
        }
    }

    public ResponseDto updateUser(Long userId, UserDto dto) {
        Optional<Users> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setProfileImageUrl(dto.getProfileImageUrl());
            userRepo.save(user);
            return new ResponseDto(200, "User updated successfully!");
        } else {
            return new ResponseDto(404, "User with ID " + userId + " not found.");
        }
    }

    public ResponseDto deleteUser(Long userId) {
        if (userRepo.existsById(userId)) {
            userRepo.deleteById(userId);
            return new ResponseDto(200, "User deleted successfully!");
        } else {
            return new ResponseDto(404, "User with ID " + userId + " not found.");
        }
    }
}
