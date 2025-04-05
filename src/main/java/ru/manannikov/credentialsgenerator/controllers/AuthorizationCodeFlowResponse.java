package ru.manannikov.credentialsgenerator.controllers;

public record AuthorizationCodeFlowResponse(
    String authorizationRequest,
    String codeVerifier
) {}