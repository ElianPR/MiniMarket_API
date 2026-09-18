package com.elian.market.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String email;
    private Set<String> roles;
}