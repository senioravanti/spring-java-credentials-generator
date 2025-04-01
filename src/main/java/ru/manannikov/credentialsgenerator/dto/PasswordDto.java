package ru.manannikov.credentialsgenerator.dto;


public record PasswordDto(
    String rawPassword,
    String pbkdf2Password,
    String salt
) {
}
