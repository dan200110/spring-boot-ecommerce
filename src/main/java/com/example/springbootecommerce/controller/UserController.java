package com.example.springbootecommerce.controller;

import com.example.springbootecommerce.dto.userentity.UserEntityChangedPasswordDto;
import com.example.springbootecommerce.dto.userentity.UserEntityCreateDto;
import com.example.springbootecommerce.dto.userentity.UserEntityIndexDto;
import com.example.springbootecommerce.exception.PasswordChangeException;
import com.example.springbootecommerce.mapper.userentity.UserEntityIndexDtoMapper;
import com.example.springbootecommerce.model.UserEntity;
import com.example.springbootecommerce.service.interfaces.UserServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserServiceInterface userServiceInterface;
    private final UserEntityIndexDtoMapper userEntityIndexDtoMapper;

    @PostMapping
    public ResponseEntity<UserEntityIndexDto> create(
            @RequestBody UserEntityCreateDto userEntityCreateDto) {
        UserEntity userEntity = userServiceInterface.registerUser(userEntityCreateDto);
        UserEntityIndexDto userEntityIndexDto = userEntityIndexDtoMapper.toDto(userEntity);
        return ResponseEntity.ok(userEntityIndexDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntityIndexDto> findById(@PathVariable("id") int id) {
        UserEntity user = userServiceInterface.findUserById(id);
        UserEntityIndexDto userEntityIndexDto = userEntityIndexDtoMapper.toDtoWithCustomRoles(user);
        return ResponseEntity.ok(userEntityIndexDto);
    }

    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<UserEntityIndexDto>> index(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserEntityIndexDto> userPage = userServiceInterface.findByCondition(pageable);
        return ResponseEntity.ok(userPage);
    }

    @PutMapping("/change_password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal(expression = "username") String username, @RequestBody UserEntityChangedPasswordDto userEntityChangedPasswordDto) {
        try {
            userServiceInterface.changePassword(username, userEntityChangedPasswordDto);
            return ResponseEntity.ok("Password changed successfully");
        } catch (PasswordChangeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
