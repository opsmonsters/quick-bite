package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.dto.UserDto;
import com.opsmonsters.quick_bite.models.Users;
import com.opsmonsters.quick_bite.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public ResponseDto createUser(UserDto dto) {
        try {
            Optional<Users> existingUser = userRepo.findByEmail(dto.getEmail());
            if (existingUser.isPresent()) {
                return new ResponseDto(400, "User with email " + dto.getEmail() + " already exists!");
            }

            Users user = convertToEntity(dto); // Convert DTO to entity
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
            userRepo.save(user);

            return new ResponseDto(200, "User created successfully!");
        } catch (Exception e) {
            return new ResponseDto(500, "Error occurred while creating the user: " + e.getMessage());
        }
    }


    public ResponseDto updateUser(long userId, UserDto dto) {
        try {
            Optional<Users> existingUser = userRepo.findById(userId);
            if (!existingUser.isPresent()) {
                return new ResponseDto(404, "User not found!");
            }

            Users user = existingUser.get();
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setProfileImageUrl(dto.getProfileImageUrl());
            userRepo.save(user);

            return new ResponseDto(200, "User updated successfully!");
        } catch (Exception e) {
            return new ResponseDto(500, "Error occurred while updating the user: " + e.getMessage());
        }
    }


    public List<UserDto> getAllUsers() {
        try {
            return userRepo.findAll().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving all users: " + e.getMessage());
        }
    }


    public ResponseDto getUserById(long userId) {
        try {
            Optional<Users> user = userRepo.findById(userId);
            if (user.isEmpty()) {
                return new ResponseDto(404, "User with ID " + userId + " not found.");
            }
            UserDto userDto = convertToDto(user.get());
            return new ResponseDto(200, userDto);
        } catch (Exception e) {
            return new ResponseDto(500, "Error occurred while retrieving the user: " + e.getMessage());
        }
    }


    public ResponseDto deleteUser(long userId) {
        try {
            Optional<Users> user = userRepo.findById(userId);
            if (!user.isPresent()) {
                return new ResponseDto(404, "User not found!");
            }
            userRepo.deleteById(userId); // Delete user from database
            return new ResponseDto(200, "User deleted successfully!");
        } catch (Exception e) {
            return new ResponseDto(500, "Error occurred while deleting the user: " + e.getMessage());
        }
    }


    private Users convertToEntity(UserDto dto) {
        Users user = new Users();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setProfileImageUrl(dto.getProfileImageUrl());

        return user;
    }


    private UserDto convertToDto(Users user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        return dto;
    }
}
