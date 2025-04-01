package ru.manannikov.credentialsgenerator.services;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.manannikov.credentialsgenerator.dto.PasswordDto;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static ru.manannikov.credentialsgenerator.utils.PasswordGeneratorUtils.*;


@Service("passwordGenerator")
public class PasswordGeneratorService {
    private static final Logger logger = LogManager.getLogger(PasswordGeneratorService.class);

    @Value("${app.password-policy.min-length}")
    private Short minLength;
    @Value("${app.password-policy.max-length}")
    private Short maxLength;
    @Value("${app.password-policy.min-unique-chars-count}")
    private Short minUniqueCharsCount;

    @Value("${app.password-policy.salt-length}")
    private Integer saltLength;
    @Value("${app.password-policy.hashing-iterations}")
    private Integer iterationCount;
    @Value("${app.password-policy.hashing-algorithm}")
    private String hashingAlgorithm;

    @Value("${app.password-policy.key-length}")
    private Integer keyLength;

    private String encodePassword(String rawPassword, byte[] salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        char[] chars = rawPassword.toCharArray();

        // PBE -> password-based encryption
        // Хранит пароль как массив символов, а не как строку
        PBEKeySpec spec = new PBEKeySpec(
            chars,
            salt,
            iterationCount,
            keyLength
        );
        SecretKeyFactory skf = SecretKeyFactory.getInstance(hashingAlgorithm);

        byte[] pbkdf2Password = skf.generateSecret(spec).getEncoded();

        return Base64.getEncoder().encodeToString(pbkdf2Password);
    }

    public PasswordDto generatePbkdf2Password()
    {
        String rawPassword = null;
        long uniqueCharsCount = 0L;
        do {
            rawPassword = generateRawPassword(
                choosePasswordLength(minLength, maxLength)
            );
            uniqueCharsCount = rawPassword.chars().distinct().count();
        } while (uniqueCharsCount < minUniqueCharsCount);

        logger.debug("rawPassword: {}", rawPassword);

        byte[] saltInBytes = generateSalt(saltLength);

        try {
            String pbkdf2Password = encodePassword(rawPassword, saltInBytes);
            String salt = Base64.getEncoder().encodeToString(saltInBytes);

            return new PasswordDto(
                rawPassword,
                pbkdf2Password,
                salt
            );
        } catch(Exception ex) {
            logger.error("Произошла ошибка при генерации хеша пароля:\n{}", ex.toString());
            throw new RuntimeException(ex);
        }
    }
}
