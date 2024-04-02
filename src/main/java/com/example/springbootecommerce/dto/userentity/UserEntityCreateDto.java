package com.example.springbootecommerce.dto.userentity;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class UserEntityCreateDto {
    private String email;
    private String username;
    private String password;
    private boolean enabled;
    private List<String> roleNames = Collections.singletonList("USER");
}
