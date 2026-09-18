package com.elian.market.api.security;

import com.elian.market.api.domain.Role;
import com.elian.market.api.domain.User;
import com.elian.market.api.dto.AuthResponse;
import com.elian.market.api.dto.LoginRequest;
import com.elian.market.api.dto.RegisterRequest;
import com.elian.market.api.exception.ResourceNotFoundException;
import com.elian.market.api.repository.RoleRepository;
import com.elian.market.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese email");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rol USER no encontrado. Verifica el DataLoader."));

        User user = User.builder()
                .name(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);

        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (org.springframework.security.core.AuthenticationException ex) {
            throw new BadCredentialsException("Email o contraseña incorrectos");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {

        CustomUserDetails userDetails = new CustomUserDetails(user);

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
