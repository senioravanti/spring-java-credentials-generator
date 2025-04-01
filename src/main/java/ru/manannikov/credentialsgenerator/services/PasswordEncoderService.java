package ru.manannikov.credentialsgenerator.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordEncoderService {
    private final PasswordEncoder passwordEncoder;

    public String hashPassword(
        String rawPassword
    ) {
        return passwordEncoder.encode(rawPassword);
    }
}
