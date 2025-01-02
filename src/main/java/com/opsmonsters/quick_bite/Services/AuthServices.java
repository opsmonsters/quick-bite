package com.opsmonsters.quick_bite.Services;

import com.opsmonsters.quick_bite.models.UserDetails;
import com.opsmonsters.quick_bite.models.Users;
import com.opsmonsters.quick_bite.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {


    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtServices jwtService;

        public AuthServices(UserRepo userRepo,
                            PasswordEncoder passwordEncoder,
                            JwtServices jwtService,
                            AuthenticationManager authenticationManager) {
            this.userRepo = userRepo;
            this.passwordEncoder = passwordEncoder;
            this.jwtService = jwtService;
            this.authenticationManager = authenticationManager;
        }

        public String register(UserDetails userDetails) {
            userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            userRepo.save(userDetails);
            return jwtService.generateToken(userDetails);
        }

        public String authenticate(String email, String password) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            Users userDetails = userRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return jwtService.generateToken(userDetails);
        }
    }


