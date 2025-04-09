package ru.manannikov.credentialsgenerator.dto;

public record CredentialsResponse(
    CredentialsDto<BCryptPasswordDto> credentialsJson,
    String credentialsCsv
) {}
