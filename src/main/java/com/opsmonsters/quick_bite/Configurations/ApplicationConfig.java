package com.opsmonsters.quick_bite.Configurations;

import com.opsmonsters.quick_bite.Services.AuthServices;
import com.opsmonsters.quick_bite.repositories.UserDetailsRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {



        private final UserDetailsRepo userRepo;

        public ApplicationConfig(UserDetailsRepo userRepo) {
            this.userRepo = userRepo;
        }

        @Bean
        public AuthServices userDetailsService() {
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
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setAuthServices(userDetailsService());
            authProvider.setPasswordEncoder(passwordEncoder());
            return authProvider;
        }
    }
}
