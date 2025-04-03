package ru.manannikov.credentialsgenerator.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.manannikov.credentialsgenerator.dto.BCryptPasswordDto;
import ru.manannikov.credentialsgenerator.dto.CredentialsDto;
import ru.manannikov.credentialsgenerator.dto.Pbkdf2PasswordDto;
import ru.manannikov.credentialsgenerator.services.PasswordGeneratorService;
import ru.manannikov.credentialsgenerator.utils.UsernameGenerator;

import java.util.UUID;

@Slf4j
@RequestMapping("/credentials")
@RestController
@RequiredArgsConstructor
public class CredentialsController {
    private final PasswordGeneratorService passwordGeneratorService;

    // Должен принимать dto с password policy для генератора.
    @GetMapping("/pbkdf2")
    public Pbkdf2PasswordDto generatePassword() {
        logger.info("generating password ...");
        return passwordGeneratorService.generatePbkdf2Password();
    }

    @GetMapping("/bcrypt")
    public CredentialsDto<BCryptPasswordDto> generateBcryptCredentials() {
        logger.info("generating bcrypt password ...");
        return new CredentialsDto<>(
            UUID.randomUUID().toString(),
            passwordGeneratorService.generateBcryptPassword()
        );
    }

    @GetMapping("/username")
    public String generateUsername(
        @RequestParam("full_name") String fullName
    ) {
        logger.info("Обработка запроса на имени пользователя: {}", fullName);
        return UsernameGenerator.transliterate(fullName);
    }
}
