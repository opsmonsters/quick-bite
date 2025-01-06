package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.dto.ForgotPasswordDto;
import com.opsmonsters.quick_bite.dto.ResetPasswordDto;
import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.models.Users;
import com.opsmonsters.quick_bite.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServices {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public ResponseDto createUser(Users user) {
        try {
            Optional<Users> existingUser = userRepo.findByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                return new ResponseDto(400, "User with email " + user.getEmail() + " already exists!");
            }
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
            return new ResponseDto(200, user.get());
        } catch (Exception e) {
            return new ResponseDto(500, "Error occurred while retrieving the user: " + e.getMessage());
        }
    }


    public ResponseDto getAllUsers() {
        try {
            return new ResponseDto(200, userRepo.findAll());
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
        Optional<Users> userOptional = userRepo.findByEmail(forgotPasswordDto.getEmail());
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 1); // Token expires in 1 hour
            user.setResetTokenExpiry(calendar.getTime());
            userRepo.save(user);


            return new ResponseDto(200, "Password reset token has been sent to your email.");
        } else {
            return new ResponseDto(404, "User not found.");
        }
    }


    public ResponseDto resetPassword(ResetPasswordDto resetPasswordDto) {
        Optional<Users> userOptional = userRepo.findByResetToken(resetPasswordDto.getToken());
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            if (user.getResetTokenExpiry().before(new Date())) {
                return new ResponseDto(400, "Reset token has expired.");
            }
            user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepo.save(user);
            return new ResponseDto(200, "Password has been reset successfully.");
        } else {
            return new ResponseDto(404, "Invalid reset token.");
        }
    }
}
