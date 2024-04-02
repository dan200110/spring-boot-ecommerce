package com.example.springbootecommerce.controller;

import com.example.springbootecommerce.dto.userentity.UserEntityCreateDto;
import com.example.springbootecommerce.dto.userentity.UserEntityLoggedInDto;
import com.example.springbootecommerce.mapper.userentity.UserEntityLoggedInDtoMapper;
import com.example.springbootecommerce.model.MyUserDetails;
import com.example.springbootecommerce.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/login")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private final UserEntityLoggedInDtoMapper userEntityLoggedInDtoMapper;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody UserEntityCreateDto userEntityCreateDto) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userEntityCreateDto.getUsername(), userEntityCreateDto.getPassword()));

            if (authentication.isAuthenticated()) {
                MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
                UserEntityLoggedInDto userEntityLoggedInDto =
                        userEntityLoggedInDtoMapper.toDto(myUserDetails.getUser());
                userEntityLoggedInDto.setJwt(jwtUtil.generateJwtToken(myUserDetails));
                return new ResponseEntity<>(userEntityLoggedInDto, HttpStatus.OK);
            }
        } catch (AuthenticationException e) {
            // Handle authentication failure
            log.error("Login failed for user: {}", userEntityCreateDto.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login failed: " + e.getMessage());
        }

        // Return error response if authentication fails
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Login failed: Invalid credentials");
    }
}
