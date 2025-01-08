package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.dto.LoginDto;
import com.opsmonsters.quick_bite.models.Users;
import com.opsmonsters.quick_bite.repositories.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServices {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtServices jwtService;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthServices.class);

    public AuthServices(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder,
            JwtServices jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public String register(Users user) {
        logger.info("Registering user with email: {}", user.getEmail());


        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        logger.info("User registered successfully with email: {}", user.getEmail());


        return jwtService.generateToken(user.getEmail(), user.getRole());
    }


    public String authenticate(String email, String password) {
        logger.info("Authenticating user with email: {}", email);

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (Exception e) {
            logger.error("Authentication failed for email: {}", email);
            throw new RuntimeException("Invalid username or password");
        }


        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        logger.info("Authentication successful for email: {}", email);


        return jwtService.generateToken(user.getEmail(), user.getRole());
    }


    public ResponseDto userLogin(LoginDto dto) {
        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    dto.getUsername(),
                    dto.getPassword()
            ));


            Optional<Users> userOptional = userRepo.findByEmail(dto.getUsername());
            if (userOptional.isEmpty()) {
                return new ResponseDto(404, "Email does not exist");
            }


            Users user = userOptional.get();
            String jwtToken = jwtService.generateToken(user.getEmail(), user.getRole());

            return new ResponseDto(200, jwtToken);

        } catch (BadCredentialsException badCredentials) {
            return new ResponseDto(403, "Username / password is incorrect");
        } catch (Exception e) {
            logger.error("An error occurred during login: {}", e.getMessage());
            return new ResponseDto(500, "An internal error occurred");
        }
    }
}
