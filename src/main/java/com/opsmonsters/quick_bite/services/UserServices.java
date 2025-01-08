package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.dto.ForgotPasswordDto;
import com.opsmonsters.quick_bite.dto.ResetPasswordDto;
import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.dto.UserDto;
import com.opsmonsters.quick_bite.models.Users;
import com.opsmonsters.quick_bite.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServices {

    @Autowired
    private UserRepo userRepo;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseDto createUser(Users user) {
        try {
            Optional<Users> existingUser = userRepo.findByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                return new ResponseDto(400, "User with email " + user.getEmail() + " already exists!");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));  // Hash the password before saving
            userRepo.save(user);
            return new ResponseDto(200, "User created successfully!");
        } catch (Exception e) {
            return new ResponseDto(500, "Error occurred while creating the user: " + e.getMessage());
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

    public ResponseDto getAllUsers() {
        try {
            List<UserDto> userDtoList = userRepo.findAll().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseDto(200, userDtoList);
        } catch (Exception e) {
            return new ResponseDto(500, "Error occurred while retrieving users: " + e.getMessage());
        }
    }

    public ResponseDto updateUser(long userId, Users updatedUser) {
        try {
            Optional<Users> optionalUser = userRepo.findById(userId);
            if (optionalUser.isPresent()) {
                Users user = optionalUser.get();
                user.setFirstName(updatedUser.getFirstName());
                user.setLastName(updatedUser.getLastName());
                user.setEmail(updatedUser.getEmail());
                user.setPhoneNumber(updatedUser.getPhoneNumber());
                user.setProfileImageUrl(updatedUser.getProfileImageUrl());
                userRepo.save(user);
                return new ResponseDto(200, "User updated successfully!");
            } else {
                return new ResponseDto(404, "User with ID " + userId + " not found.");
            }
        } catch (Exception e) {
            return new ResponseDto(500, "Error occurred while updating the user: " + e.getMessage());
        }
    }

    public ResponseDto deleteUser(long userId) {
        try {
            if (userRepo.existsById(userId)) {
                userRepo.deleteById(userId);
                return new ResponseDto(200, "User deleted successfully!");
            } else {
                return new ResponseDto(404, "User with ID " + userId + " not found.");
            }
        } catch (Exception e) {
            return new ResponseDto(500, "Error occurred while deleting the user: " + e.getMessage());
        }
    }

    public Users getUserByEmail(String email) {
        try {
            Optional<Users> user = userRepo.findByEmail(email);
            return user.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving the user by email: " + e.getMessage(), e);
        }
    }

    public ResponseDto forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        try {
            Optional<Users> userOptional = userRepo.findByEmail(forgotPasswordDto.getEmail());
            if (userOptional.isPresent()) {
                Users user = userOptional.get();

                String resetToken = UUID.randomUUID().toString();
                user.setResetToken(resetToken);  // Save the reset token in the user
                userRepo.save(user);


                return new ResponseDto(200, "Password reset link sent to your email with the token: " + resetToken);
            } else {
                return new ResponseDto(404, "User with email " + forgotPasswordDto.getEmail() + " not found.");
            }
        } catch (Exception e) {
            return new ResponseDto(500, "Error occurred while processing the forgot password request: " + e.getMessage());
        }
    }


    public ResponseDto resetPassword(ResetPasswordDto resetPasswordDto) {
        try {

            Optional<Users> userOptional = userRepo.findByResetToken(resetPasswordDto.getToken());
            if (userOptional.isPresent()) {
                Users user = userOptional.get();


                user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
                user.setResetToken(null);
                userRepo.save(user);

                return new ResponseDto(200, "Password has been reset successfully.");
            } else {
                return new ResponseDto(404, "Invalid or expired password reset token.");
            }
        } catch (Exception e) {
            return new ResponseDto(500, "Error occurred while resetting the password: " + e.getMessage());
        }
    }


    private UserDto convertToDto(Users user) {
        try {
            UserDto userDto = new UserDto();
            userDto.setUserId(user.getUserId());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setPhoneNumber(user.getPhoneNumber());
            userDto.setProfileImageUrl(user.getProfileImageUrl());
            userDto.setCreatedAt(user.getCreatedAt());
            userDto.setUpdatedAt(user.getUpdatedAt());
            return userDto;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while converting user to DTO: " + e.getMessage(), e);
        }
    }
}
