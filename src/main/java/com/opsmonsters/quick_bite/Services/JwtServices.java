package com.opsmonsters.quick_bite.Services;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtServices {



        private final String SECRET_KEY = "1234hjksl_24567839hjkkkskx_ljhhhh";

        public String generateToken(String userName) {
            Map<String, Object> claims = new HashMap<>();
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userName)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
        }

        public String extractUsername(String token) {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
        }

        public Boolean validateToken(String token, String username) {
            return username.equals(extractUsername(token)) && !isTokenExpired(token);
        }

        private Boolean isTokenExpired(String token) {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration().before(new Date());
        }
    }

