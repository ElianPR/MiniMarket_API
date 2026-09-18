package com.elian.market.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserResponseDto {

    private Long id;
    private String fullName;
    private String email;
    private Set<String> roles;
    private LocalDateTime createdAt;
}