package com.opsmonsters.quick_bite.Services;

import com.opsmonsters.quick_bite.models.Users;
import com.opsmonsters.quick_bite.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {



        @Autowired
        UserRepo userRepo;

        public List<Users> getAllUsers() {
            return userRepo.findAll();
        }

        public Users getUserById(Long id) {
            return userRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        }

        public Users createUser(Users user) {
            return userRepo.save(user);
        }

        public Users updateUser(Long id, Users userDetails) {
            Users existingUser = getUserById(id);

            existingUser.setFirstName(userDetails.getFirstName());
            existingUser.setLastName(userDetails.getLastName());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setPhoneNumber(userDetails.getPhoneNumber());
            existingUser.setProfileImageUrl(userDetails.getProfileImageUrl());
            existingUser.setPassword(userDetails.getPassword());

            return userRepo.save(existingUser);
        }

        public void deleteUser(Long id) {
            userRepo.deleteById(id);
        }
    }

