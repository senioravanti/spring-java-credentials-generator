package ru.manannikov.credentialsgenerator.dto;


public record Pbkdf2PasswordDto(
    String rawPassword,
    String pbkdf2Password,
    String salt
) {}