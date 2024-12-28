package com.opsmonsters.quick_bite.Configurations;

import com.opsmonsters.quick_bite.Services.AuthServices;
import com.opsmonsters.quick_bite.repositories.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {



    private final UserRepo userRepo;

    public ApplicationConfig(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Bean
    public AuthServices DetailsService() {
        return username -> userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        AuthenticationProvider authProvider = new AuthenticationProvider();
        authProvider.setAuthServices(new AuthServices());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}

