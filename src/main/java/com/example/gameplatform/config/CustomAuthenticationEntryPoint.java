package com.example.gameplatform.config;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String errorMessage = "Authentication failed";
        Throwable cause = authException.getCause();

        if (authException instanceof BadCredentialsException) {
            errorMessage = "Invalid credentials";
        } else if (cause instanceof io.jsonwebtoken.ExpiredJwtException) {
            errorMessage = "JWT token has expired";
        } else if (cause instanceof io.jsonwebtoken.JwtException) {
            errorMessage = "Invalid JWT token: " + cause.getMessage();
        }

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(String.format(
                "{ \"error\": \"Unauthorized\", \"message\": \"%s\", \"path\": \"%s\" }",
                errorMessage,
                request.getRequestURI()
        ));
    }
}
