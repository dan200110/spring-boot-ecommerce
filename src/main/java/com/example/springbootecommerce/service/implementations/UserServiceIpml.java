package com.example.springbootecommerce.service.implementations;

import com.example.springbootecommerce.dto.userentity.UserEntityChangedPasswordDto;
import com.example.springbootecommerce.dto.userentity.UserEntityCreateDto;
import com.example.springbootecommerce.dto.userentity.UserEntityIndexDto;
import com.example.springbootecommerce.exception.PasswordChangeException;
import com.example.springbootecommerce.exception.ResourceDuplicateException;
import com.example.springbootecommerce.exception.ResourceNotFoundException;
import com.example.springbootecommerce.mapper.userentity.UserEntityCreateDtoMapper;
import com.example.springbootecommerce.mapper.userentity.UserEntityIndexDtoMapper;
import com.example.springbootecommerce.model.RoleEntity;
import com.example.springbootecommerce.model.UserEntity;
import com.example.springbootecommerce.repository.RoleRepository;
import com.example.springbootecommerce.repository.UserRepository;
import com.example.springbootecommerce.service.interfaces.UserServiceInterface;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceIpml implements UserServiceInterface {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserEntityCreateDtoMapper userEntityCreateDtoMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityIndexDtoMapper userEntityIndexDtoMapper;

    @Override
    public UserEntity registerUser(UserEntityCreateDto userEntityCreateDto) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(userEntityCreateDto.getUsername()))) {
            throw new ResourceDuplicateException("username already exist");
        }

        UserEntity userEntity = userEntityCreateDtoMapper.toEntity(userEntityCreateDto);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        // If no roles are provided, set default role to USER
        if (userEntityCreateDto.getRoleNames() == null) {
            RoleEntity defaultRole = roleRepository.findRoleByName("ROLE_USER");
            if (defaultRole == null) {
                throw new IllegalStateException("Default role 'USER' not found in the database.");
            }
            userEntity.getRoleEntities().add(defaultRole);
        } else {
            // Otherwise, retrieve the role entities for each role name provided
            Set<RoleEntity> roles = userEntityCreateDto.getRoleNames().stream()
                    .map(roleName -> roleRepository.findRoleByName(roleName))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            userEntity.setRoleEntities(roles);
        }

        try {
            userEntity = userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            // Handle validation or other data integrity issues
            throw new IllegalArgumentException("User creation failed: " + e.getMessage());
        }

        return userEntity;
    }

    @Override
    public UserEntity findUserById(long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            return userEntity.get();
        } else {
            throw new ResourceNotFoundException("Could not find user with id " + id);
        }
    }

    @Override
    public UserEntity findUserByUserName(String userName) {
        Optional<UserEntity> userEntity = userRepository.findUserByUsername(userName);
        if (userEntity.isPresent()) {
            return userEntity.get();
        } else {
            throw new ResourceNotFoundException("Could not find user with id " + userName);
        }
    }

    public Page<UserEntityIndexDto> findByCondition(Pageable pageable) {
        return userRepository.findAll(pageable).map(userEntityIndexDtoMapper::toDto);
    }

    @Override
    public void changePassword(String username, UserEntityChangedPasswordDto userEntityChangedPasswordDto) {
        Optional<UserEntity> user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new PasswordChangeException("User not found");
        }

        // Verify the current password
        if (!passwordEncoder.matches(userEntityChangedPasswordDto.getOldPassword(), user.get().getPassword())) {
            throw new PasswordChangeException("Current password is incorrect");
        }

        // Encode and set the new password
        String newPasswordEncoded = passwordEncoder.encode(userEntityChangedPasswordDto.getNewPassword());
        user.get().setPassword(newPasswordEncoded);

        // Save the updated user entity
        userRepository.save(user.get());
    }

}
