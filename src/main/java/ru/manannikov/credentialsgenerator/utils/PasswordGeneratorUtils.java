package ru.manannikov.credentialsgenerator.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class PasswordGeneratorUtils {
    private static final Logger logger = LogManager.getLogger(PasswordGeneratorUtils.class);

    private static final Random random = new SecureRandom();
    private static final String SPECIAL_CHARACTERS = "~!@#$%^&*()_-+=;<>.?";

    public static int[] choosePasswordLength(int minLen, int maxLen) {
        var passLen = random.nextInt(minLen, maxLen + 1);

        var chars = new int[minLen];
        Arrays.fill(chars, 1);

        for (int i = 0; i < passLen - minLen; ++i) {
            var index = random.nextInt(0, minLen);
            chars[index]++;
        }

        return chars;
    }

    private static Stream<Character> getRandomSpecialChars(int count) {
        Character[] specialChars = new Character[count];
        for (int i = 0; i < count; ++i) {
            int specialChar = random.nextInt(0, SPECIAL_CHARACTERS.length());
            specialChars[i] = SPECIAL_CHARACTERS.charAt(specialChar);
        }
        return Arrays.stream(specialChars);
    }

    private static Stream<Character> getRandomChars(int count, int lowerBound, int upperBound) {
        return random
            .ints(count, lowerBound, upperBound)
            .mapToObj(d -> (char) d)
            ;
    }

    public static String generateRawPassword(int[] charCategoryNumbers) {
        Stream<Character> passwordStream = Stream
            .concat(
                // Спец символы
                getRandomSpecialChars(charCategoryNumbers[0]),

                Stream.concat(
                    // Цифры
                    getRandomChars(charCategoryNumbers[1], 48, 58),

                    Stream.concat(
                        // Буквы в верхнем регистре
                        getRandomChars(charCategoryNumbers[2], 65, 91),

                        // Буквы в нижнем регистре
                        getRandomChars(charCategoryNumbers[3], 97, 123)
                    )
                )

            );
        var chars = passwordStream.collect(Collectors.toCollection(ArrayList::new));

        // Список должен быть изменяемым
        Collections.shuffle(chars, random);

        return chars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

    public static byte[] generateSalt(int saltLength) {
        byte[] salt = new byte[saltLength];

        random.nextBytes(salt);

        return salt;
    }
}
