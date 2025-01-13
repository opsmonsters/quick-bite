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
import java.util.stream.Stream;

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

            if (dto.getRole() == null || dto.getRole().isEmpty()) {
                user.setRole("USER");
            } else {
                user.setRole(dto.getRole());
            }

            Users savedUser = userRepo.save(user);

            UserDto userDto = Stream.of(savedUser)
                    .map(u -> {
                        UserDto dtoResponse = new UserDto();
                        dtoResponse.setUserId(u.getUserId());
                        dtoResponse.setFirstName(u.getFirstName());
                        dtoResponse.setLastName(u.getLastName());
                        dtoResponse.setEmail(u.getEmail());
                        dtoResponse.setPhoneNumber(u.getPhoneNumber());
                        dtoResponse.setProfileImageUrl(u.getProfileImageUrl());
                        dtoResponse.setRole(u.getRole());
                        dtoResponse.setCreatedAt(u.getCreatedAt());
                        dtoResponse.setUpdatedAt(u.getUpdatedAt());
                        return dtoResponse;
                    })
                    .findFirst()
                    .orElse(null);


            return new ResponseDto(201, "User created successfully!", userDto);
        } catch (Exception e) {
            return new ResponseDto(500, "Error while creating user: " + e.getMessage(), null);
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
                    dto.setPassword(user.getPassword());
                    dto.setPhoneNumber(user.getPhoneNumber());
                    dto.setProfileImageUrl(user.getProfileImageUrl());
                    dto.setRole(user.getRole());
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

            if (dto.getRole() != null && !dto.getRole().isEmpty()) {
                user.setRole(dto.getRole());
            }

            userRepo.save(user);
            return new ResponseDto(200, "User updated successfully!");
        } else {
            return new ResponseDto(404, "User with ID " + userId + " not found.");
        }
    }
    public Optional<Users> getUserByEmail(String email) {
        try {

            Optional<Users> userOptional = userRepo.findByEmail(email);


            return userOptional;

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving the user by email: " + e.getMessage(), e);
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

