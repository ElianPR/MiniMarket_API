package com.elian.market.api.data;

import com.elian.market.api.domain.Role;
import com.elian.market.api.domain.User;
import com.elian.market.api.repository.RoleRepository;
import com.elian.market.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {

        // Evita duplicar datos cada vez que se reinicia la app
        if (roleRepository.count() > 0) {
            return;
        }

        // =========================
        // ROLES
        // =========================

        Role userRole = Role.builder()
                .name("USER")
                .build();

        Role adminRole = Role.builder()
                .name("ADMIN")
                .build();

        roleRepository.save(userRole);
        roleRepository.save(adminRole);


        // =========================
        // ADMIN
        // =========================

        User admin = User.builder()
                .name("Administrador")
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .roles(Set.of(adminRole))
                .build();

        userRepository.save(admin);
    }
}