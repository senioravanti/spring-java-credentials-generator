package ru.manannikov.credentialsgenerator.utils;


import lombok.extern.log4j.Log4j2;
import ru.manannikov.credentialsgenerator.dto.CodeChallengeAndVerifierDto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Log4j2
public class OAuth2Utils {
    private static final SecureRandom secureRandom = new SecureRandom();
    /* digest по анг. хэш */
    private static final MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException ex) {
            logger.error("problem initializing messageDigest:\n\texception: {}", ex.toString());
            throw new RuntimeException(ex);
        }
    }

    public static String generateState() {
        final var state = new byte[12];
        secureRandom.nextBytes(state);
        return Base64
            .getUrlEncoder()
            .encodeToString(state)
        ;
    }

    public static CodeChallengeAndVerifierDto generateSecretCodePair() {
        final var base64Encoder = Base64.getUrlEncoder().withoutPadding();

        final var randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        final var codeVerifier = base64Encoder.encodeToString(randomBytes);

        final var codeChallenge = base64Encoder.encodeToString(messageDigest.digest(codeVerifier.getBytes()));
        return new CodeChallengeAndVerifierDto(codeChallenge, codeVerifier);
    }
}