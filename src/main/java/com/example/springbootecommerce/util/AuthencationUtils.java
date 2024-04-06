package com.example.springbootecommerce.util;

import com.example.springbootecommerce.model.UserEntity;
import com.example.springbootecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthencationUtils {
    private final UserRepository userRepository;

    public UserEntity extractUserFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<UserEntity> userOptional = userRepository.findUserByUsername(username);
        return userOptional.orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
