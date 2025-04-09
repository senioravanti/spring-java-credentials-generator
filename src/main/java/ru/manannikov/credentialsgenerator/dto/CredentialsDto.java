package ru.manannikov.credentialsgenerator.dto;

public record CredentialsDto<T>(
    String uuid,
    String username,
    String email,

    String lastName,
    String firstName,
    String middleName,

    T passwordDto
) {}