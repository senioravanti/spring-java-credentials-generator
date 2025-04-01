package ru.manannikov.credentialsgenerator.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.manannikov.credentialsgenerator.dto.PasswordDto;
import ru.manannikov.credentialsgenerator.services.PasswordEncoderService;
import ru.manannikov.credentialsgenerator.services.PasswordGeneratorService;
import ru.manannikov.credentialsgenerator.utils.UsernameGenerator;

/**
 * curl -i -w '\n' -X POST -d 'raw_password=124356' http://localhost:8000/api/credentials/hash
 */

@Slf4j
@RequestMapping("/credentials")
@RestController
@RequiredArgsConstructor
public class CredentialsController {
    private final PasswordGeneratorService passwordGeneratorService;
    private final PasswordEncoderService passwordEncoderService;

    // Должен принимать dto с password policy для генератора.
    @GetMapping("/generate")
    public PasswordDto generatePassword() {
        logger.info("generating password ...");
        return passwordGeneratorService.generatePbkdf2Password();
    }

    @PostMapping("/hash")
    public String hashPassword(
        @RequestParam(name = "raw_password") String rawPassword
    ) {
        logger.info("hashing raw password ...");
        return passwordEncoderService.hashPassword(rawPassword);
    }

    @GetMapping("/username")
    public String generateUsername(
        @RequestParam("full_name") String fullName
    ) {
        logger.info("Обработка запроса на имени пользователя: {}", fullName);
        return UsernameGenerator.transliterate(fullName);
    }
}
