package com.elian.market.api.security;

import com.elian.market.api.domain.User;
import com.elian.market.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        return new CustomUserDetails(user);
    }
}