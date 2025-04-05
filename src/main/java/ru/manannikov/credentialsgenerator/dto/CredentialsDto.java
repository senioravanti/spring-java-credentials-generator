package ru.manannikov.credentialsgenerator.dto;

public record CredentialsDto<T>(
    String uuid,
    String username,
    T passwordDto
) {}