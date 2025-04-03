package ru.manannikov.credentialsgenerator.dto;

public record OAuth2AuthorizationRequest(
    String scope,

    String clientHost,
    String clientPort,
    String clientId,

    String serverHost,
    String serverPort
) {}
