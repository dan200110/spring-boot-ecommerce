package com.example.springbootecommerce.dto.userentity;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserEntityIndexDto {

    private int id;
    private String email;
    private Boolean enabled;
    private String username;
    private Set<String> roles;
}