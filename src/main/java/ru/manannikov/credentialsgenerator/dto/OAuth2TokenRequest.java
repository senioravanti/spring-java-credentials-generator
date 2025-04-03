package ru.manannikov.credentialsgenerator.dto;

public record OAuth2TokenRequest(
    String serverHost,
    String serverPort,

    String clientId,
    String clientSecret,

    String redirectUri,

    String code,
    String codeVerifier
) {}