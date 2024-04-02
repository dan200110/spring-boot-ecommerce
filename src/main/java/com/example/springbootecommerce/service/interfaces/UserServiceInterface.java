package com.example.springbootecommerce.service.interfaces;

import com.example.springbootecommerce.dto.userentity.UserEntityChangedPasswordDto;
import com.example.springbootecommerce.dto.userentity.UserEntityCreateDto;
import com.example.springbootecommerce.dto.userentity.UserEntityIndexDto;
import com.example.springbootecommerce.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserServiceInterface {
    UserEntity registerUser(UserEntityCreateDto userEntityCreateDto);

    UserEntity findUserById(long id);

    UserEntity findUserByUserName(String userName);

    Page<UserEntityIndexDto> findByCondition(Pageable pageable);

    void changePassword(String username, UserEntityChangedPasswordDto userEntityChangedPasswordDto);

}
