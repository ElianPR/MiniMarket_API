package com.elian.market.api.service;

import com.elian.market.api.domain.User;
import com.elian.market.api.dto.UserResponseDto;
import com.elian.market.api.exception.ResourceNotFoundException;
import com.elian.market.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );
    }

    @Override
    public List<UserResponseDto> findAll() {

        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public UserResponseDto findById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Usuario no encontrado con id: " + id
                        )
                );

        return toResponse(user);
    }

    private UserResponseDto toResponse(User user) {

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
}