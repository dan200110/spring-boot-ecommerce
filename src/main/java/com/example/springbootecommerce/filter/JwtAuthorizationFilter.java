package com.example.springbootecommerce.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.springbootecommerce.exception.InvalidCredentialException;
import com.example.springbootecommerce.model.MyUserDetails;
import com.example.springbootecommerce.service.implementations.UserDetailServiceImpl;
import com.example.springbootecommerce.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailServiceImpl userDetailService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = checkTokenFormatThenReturnToken(request);
            checkAuthenticationFromToken(jwt);
        } catch (InvalidCredentialException invalidCredentialException) {
            log.error(invalidCredentialException.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, invalidCredentialException);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();

        if (request.getMethod().equals("POST")
                && (path.contains("/api/v1/users")
                || path.equals("/api/v1/login")
                || path.equals("/api/v1/orders/with_email")
                || path.equals("/api/v1/orders/with_user"))) {
            return true;
        } else if (request.getMethod().equals("GET")
                && (path.contains("/api/v1/products") || path.equals("/api/v1/categories")
                || path.contains("/api/v1/users")
        )) {
            return true;
        } else if (path.split("/")[1].equals("swagger-ui") || path.equals("/swagger-ui.html")) {
            return true;
        } else if (path.split("/")[2].equals("api-docs")
                || path.equals("/v3/api-docs")
                || path.equals("/v3/api-docs.yaml")) {
            return true;
        } else {
            return false;
        }
    }

    private String checkTokenFormatThenReturnToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth == null) throw new InvalidCredentialException("missing authorization token");

        if (headerAuth.startsWith("Bearer ")) {
            String jwt = headerAuth.substring(7);

            try {
                if (jwtUtil.validateJwtToken(jwt)) {
                    return jwt;
                }
            } catch (JWTDecodeException jwtDecodeException) {
                throw new InvalidCredentialException("wrong token format");
            }
        }

        throw new InvalidCredentialException("wrong token format");
    }

    private void checkAuthenticationFromToken(String jwt) {
        String username = jwtUtil.getUserNameFromJwtToken(jwt);

        if (username != null && !username.isEmpty()) {
            try {
                MyUserDetails myUserDetails = userDetailService.loadUserByUsername(username);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                myUserDetails, null, myUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (UsernameNotFoundException usernameNotFoundException) {
                throw new InvalidCredentialException(usernameNotFoundException.getMessage());
            }
        } else {
            throw new InvalidCredentialException("name is empty");
        }
    }
}
