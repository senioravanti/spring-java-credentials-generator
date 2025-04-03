package ru.manannikov.credentialsgenerator.dto;

public record CodeChallengeAndVerifierDto(
    String codeChallenge,
    String codeVerifier
) {}