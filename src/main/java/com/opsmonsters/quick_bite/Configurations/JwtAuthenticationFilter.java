package com.opsmonsters.quick_bite.Configurations;

import com.opsmonsters.quick_bite.models.Users;
import com.opsmonsters.quick_bite.services.JwtServices;
import com.opsmonsters.quick_bite.services.UserServices;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServices jwtService;
    private final UserServices userServices;

    public JwtAuthenticationFilter(JwtServices jwtService, UserServices userServices) {
        this.jwtService = jwtService;
        this.userServices = userServices;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        String userEmail;

        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (ExpiredJwtException e) {
            handleException(response, HttpServletResponse.SC_BAD_REQUEST, "Your Token has expired");
            return;
        } catch (Exception e) {
            handleException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid Token");
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (userEmail != null && authentication == null) {
            try {
                Users userDetails = userServices.getUserByEmail(userEmail);

                if (userDetails != null && jwtService.isTokenValid(jwt, String.valueOf(userDetails))) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    String role = jwtService.extractClaim(jwt, claims -> claims.get("role", String.class));

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (UsernameNotFoundException e) {
                handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - User Not Found");
                return;
            } catch (Exception e) {
                handleException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing authentication");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
