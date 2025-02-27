package com.opsmonsters.quick_bite.Configurations;

import com.opsmonsters.quick_bite.models.Users;
import com.opsmonsters.quick_bite.services.BlackListService;

import com.opsmonsters.quick_bite.services.JwtServices;
import com.opsmonsters.quick_bite.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    private final JwtServices jwtService;
    private final UserService userService;
    private final BlackListService blacklistService;
    public JwtAuthenticationFilter(JwtServices jwtService, UserService userService, BlackListService blacklistService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestPath = request.getServletPath();
        if (requestPath.equals("/register") || requestPath.equals("/login") || requestPath.equals("/auth/logout")) {
            logger.info("Skipping authentication for: " + requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.info("No JWT token found in request headers");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);


        if (blacklistService.isTokenBlacklisted(jwt)) {
            logger.warning("Token is blacklisted: " + jwt);
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted. Please log in again.");
            return;
        }

        String userEmail = null;

        try {
            userEmail = jwtService.extractUsername(jwt);
            logger.info("Extracted username from JWT: " + userEmail);
        } catch (ExpiredJwtException e) {
            logger.warning("JWT token has expired: " + e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Your Token has expired");
            return;
        } catch (Exception e) {
            logger.warning("Invalid JWT token: " + e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Optional<Users> userDetails = userService.getUserByEmail(userEmail);

                if (userDetails.isPresent() && jwtService.isTokenValid(jwt, userDetails.get().getEmail())) {
                    String role = jwtService.extractClaim(jwt, claims -> claims.get("role", String.class));

                    if (role == null) {
                        logger.warning("Role is null in JWT token");
                        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
                        return;
                    }

                    logger.info("Extracted role from JWT: " + role);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails.get(),
                            null,
                            Collections.singletonList(() -> "ROLE_" + role)
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("User authenticated: " + userEmail);
                } else {
                    logger.warning("Invalid token or user not found for email: " + userEmail);
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token or User not found");
                    return;
                }
            } catch (UsernameNotFoundException e) {
                logger.warning("User not found: " + userEmail);
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - User Not Found");
                return;
            } catch (Exception e) {
                logger.severe("Error processing authentication: " + e.getMessage());
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing authentication");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
