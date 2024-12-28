package com.opsmonsters.quick_bite.Configurations;

import com.opsmonsters.quick_bite.Services.AuthServices;
import com.opsmonsters.quick_bite.Services.JwtServices;
import com.opsmonsters.quick_bite.models.Users;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.lang.NonNull;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

private final HandlerExceptionResolver handlerExceptionResolver;

private final JwtServices jwtService;
private final AuthServices detailsService;

public JwtAuthenticationFilter(
        JwtServices jwtService,
        AuthServices detailsService,
        HandlerExceptionResolver handlerExceptionResolver
) {
    this.jwtService = jwtService;
    this.detailsService = detailsService;
    this.handlerExceptionResolver = handlerExceptionResolver;
}

@Override
protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
    }

    try {
        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (userEmail != null && authentication == null) {
            Users user = this.detailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, user)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );

                authToken.setdetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    } catch (Exception exception) {

        if (exception instanceof UsernameNotFoundException ) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized - Invalid Token or User Not Found");
        } else if (exception instanceof NoHandlerFoundException) {

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Resource not found");
        }else if(exception instanceof ExpiredJwtException){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Your Token has expired");
        } else {

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            exception.printStackTrace();
            response.getWriter().write("Internal Server Error");
        }
    }
}