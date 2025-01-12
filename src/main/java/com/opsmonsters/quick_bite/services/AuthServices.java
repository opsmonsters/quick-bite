package com.opsmonsters.quick_bite.services;
import com.opsmonsters.quick_bite.dto.ForgotPasswordDto;
import com.opsmonsters.quick_bite.dto.LoginDto;
import com.opsmonsters.quick_bite.dto.ResetPasswordDto;
import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.models.Otp;
import com.opsmonsters.quick_bite.models.Users;
import com.opsmonsters.quick_bite.repositories.OtpRepo;
import com.opsmonsters.quick_bite.repositories.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthServices {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtServices jwtService;
    private final OtpRepo otpRepo;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthServices.class);


    public AuthServices(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder,
            JwtServices jwtService,
            OtpRepo otpRepo,
            AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.otpRepo= otpRepo;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
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
                .orElseThrow(() -> new RuntimeException("User not found")).getUser();

        if (!user.getIsOtpVerified()) {
            logger.error("User with email {} has not verified OTP", email);
            throw new RuntimeException("Please verify your OTP before logging in.");
        }

        return jwtService.generateToken(user.getEmail(), user.getRole());
    }

    public ResponseDto userLogin(LoginDto dto) {
        try {
            logger.info("Attempting to login with username: {}", dto.getUsername());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    dto.getUsername(),
                    dto.getPassword()
            ));

            Optional<Otp> userOptional = userRepo.findByEmail(dto.getUsername());
            if (userOptional.isEmpty()) {
                logger.error("User not found with username: {}", dto.getUsername());
                return new ResponseDto(404, "Email does not exist");
            }

            Users user = userOptional.get().getUser();
            logger.info("User found with username: {}", user.getEmail());
            String jwtToken = jwtService.generateToken(user.getEmail(), user.getRole());

            return new ResponseDto(200, jwtToken);

        } catch (BadCredentialsException badCredentials) {
            logger.error("Invalid credentials for username: {}", dto.getUsername());
            return new ResponseDto(403, "Username / password is incorrect");
        } catch (Exception e) {
            logger.error("An error occurred during login: {}", e.getMessage());
            return new ResponseDto(500, "An internal error occurred");
        }
    }
    public ResponseDto forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        try {
            logger.info("Processing forgot password request for email: {}", forgotPasswordDto.getEmail());


            Optional<Otp> userOptional = userRepo.findByEmail(forgotPasswordDto.getEmail());
            if (userOptional.isEmpty()) {
                logger.error("User not found with email: {}", forgotPasswordDto.getEmail());
                return new ResponseDto(404, "User not found.");
            }


            Users user = userOptional.get().getUser();
            Long userId = user.getUserId();


            String otpCode = generateOtp();


            Otp otps = new Otp();
            otps.setOtp(otpCode);
            otps.setUserId(userId);
            otps.setCreatedAt(new Date());
            otps.setExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000));
            otps.setIsUsed(false);

            otpRepo.save(otps);

            logger.info("Generated OTP for email {}: {}", forgotPasswordDto.getEmail(), otpCode);

            return new ResponseDto(200, "OTP generated and sent successfully.");

        } catch (Exception e) {
            logger.error("An error occurred during forgot password processing: {}", e.getMessage());
            return new ResponseDto(500, "An internal error occurred.");
        }
    }


    public ResponseDto resetPassword(ResetPasswordDto resetPasswordDto) {
        try {
            logger.info("Attempting to reset password for email: {}", resetPasswordDto.getEmail());


            Optional<Otp> userOptional = userRepo.findByEmail(resetPasswordDto.getEmail());
            if (userOptional.isEmpty()) {
                logger.error("User not found with email: {}", resetPasswordDto.getEmail());
                return new ResponseDto(404, "User not found.");
            }

            Otp otp = userOptional.get();


            Optional<Otp> otpOptional = Optional.ofNullable(otpRepo.findByUserIdAndOtp(Long.parseLong(otp.getUserId().toString()), resetPasswordDto.getOtp()));
            if (otpOptional.isEmpty()) {
                logger.error("Invalid OTP for email: {}", resetPasswordDto.getEmail());
                return new ResponseDto(403, "Invalid OTP.");
            }

            Users user = userOptional.get().getUser();


            if (otp.getIsUsed()) {
                logger.error("OTP already used for email: {}", resetPasswordDto.getEmail());
                return new ResponseDto(403, "OTP already used.");
            }

            if (otp.getExpiresAt().before(new Date())) {
                logger.error("OTP expired for email: {}", resetPasswordDto.getEmail());
                return new ResponseDto(403, "OTP expired.");
            }


            user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
            userRepo.save(user);


            otp.setIsUsed(true);
            otpRepo.save(otp);

            logger.info("Password reset successfully for email: {}", resetPasswordDto.getEmail());
            return new ResponseDto(200, "Password reset successfully.");

        } catch (Exception e) {
            logger.error("An error occurred during password reset: {}", e.getMessage());
            return new ResponseDto(500, "An internal error occurred.");
        }
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}



