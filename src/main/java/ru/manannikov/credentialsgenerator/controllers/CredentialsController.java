package ru.manannikov.credentialsgenerator.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.manannikov.credentialsgenerator.dto.BCryptPasswordDto;
import ru.manannikov.credentialsgenerator.dto.CredentialsDto;
import ru.manannikov.credentialsgenerator.dto.CredentialsResponse;
import ru.manannikov.credentialsgenerator.dto.Pbkdf2PasswordDto;
import ru.manannikov.credentialsgenerator.services.PasswordGeneratorService;
import ru.manannikov.credentialsgenerator.utils.UsernameGenerator;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequestMapping("/credentials")
@RestController
@RequiredArgsConstructor
public class CredentialsController {
    private final PasswordGeneratorService passwordGeneratorService;

    @Qualifier("globalFaker")
    private final Faker globalFaker;
    @Qualifier("ruFaker")
    private final Faker ruFaker;

    // Должен принимать dto с password policy для генератора.
    @GetMapping("/pbkdf2")
    public Pbkdf2PasswordDto generatePassword() {
        logger.info("generating password ...");
        return passwordGeneratorService.generatePbkdf2Password();
    }

    @GetMapping("/bcrypt")
    public CredentialsResponse generateBcryptCredentials() {
        final String username = globalFaker.internet().username().replace('.', '-');

        final String userId = UUID.randomUUID().toString();

        final String email = globalFaker.internet().emailAddress(username);
        final String phoneNumber = ruFaker.phoneNumber().cellPhoneInternational();

        final String[] fullName = ruFaker.name().fullName().split("\\s+");
        final String lastName = fullName[0];
        final String firstName = fullName[1];
        String middleName;
        try {
            middleName = fullName[2];
        } catch (ArrayIndexOutOfBoundsException ex) {
            middleName = null;
        }

        final var passwordDto = passwordGeneratorService.generateBcryptPassword();

        final var credentialsJson = new CredentialsDto<>(
            userId,
            username,
            email, phoneNumber,
            lastName, firstName, middleName,
            passwordDto
        );

        final String credentialsCsv = String.format(
            "('%s', '%s', '%s', '%s', '%s', '%s', '%s', %s)",
            userId, username, phoneNumber,
            passwordDto.bcryptPassword(), email,
            lastName, firstName, Optional.ofNullable(middleName).map(it -> String.format("'%s'", it)).orElse("DEFAULT")
        );

        return new CredentialsResponse(
            credentialsJson,
            credentialsCsv
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
