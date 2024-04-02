package com.example.springbootecommerce.dto.userentity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntityLoggedInDto {
    private int id;
    private String email;
    private Boolean enabled;
    private String username;
    private String jwt;
}
