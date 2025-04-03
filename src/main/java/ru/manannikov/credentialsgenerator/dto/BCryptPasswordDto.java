package ru.manannikov.credentialsgenerator.dto;

public record BCryptPasswordDto(
    String rawPassword,
    String bcryptPassword
) {}