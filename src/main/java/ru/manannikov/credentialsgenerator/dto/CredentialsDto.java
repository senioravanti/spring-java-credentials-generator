package ru.manannikov.credentialsgenerator.dto;

public record CredentialsDto<T>(
    String username,
    T passwordDto
) {}