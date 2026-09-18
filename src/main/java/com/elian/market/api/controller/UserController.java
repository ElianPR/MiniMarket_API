package com.elian.market.api.controller;

import com.elian.market.api.domain.User;
import com.elian.market.api.dto.UserResponseDto;
import com.elian.market.api.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public UserResponseDto getCurrentUser() {

        User user = userService.getCurrentUser();

        return UserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getName())
                .email(user.getEmail())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(role -> role.getName())
                                .collect(java.util.stream.Collectors.toSet())
                )
                .createdAt(user.getCreatedAt())
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }
}
