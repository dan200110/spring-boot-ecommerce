package com.example.springbootecommerce.dto.userentity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntityChangedPasswordDto {
    private String oldPassword;
    private String newPassword;
}
