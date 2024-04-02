package com.example.springbootecommerce.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test_permissions")
@Slf4j
@RequiredArgsConstructor
public class TestPermissionController {
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @GetMapping("/user")
    public String userProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Authenticated user: {}", authentication.getName());
        log.debug("User authorities: {}", authentication.getAuthorities());
        return "Welcome to " + authentication.getName() + authentication.getAuthorities();
    }


    @GetMapping("/admin")
    public String adminProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Authenticated user: {}", authentication.getName());
        log.debug("User authorities: {}", authentication.getAuthorities());
        return "Welcome to " + authentication.getName() + authentication.getAuthorities();
    }
}
