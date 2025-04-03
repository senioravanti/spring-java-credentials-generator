package ru.manannikov.credentialsgenerator.controllers;

import org.springframework.web.bind.annotation.*;
import ru.manannikov.credentialsgenerator.dto.OAuth2AuthorizationRequest;
import ru.manannikov.credentialsgenerator.dto.OAuth2TokenRequest;
import ru.manannikov.credentialsgenerator.utils.OAuth2Utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
    @PostMapping("/authorization-url")
    public AuthorizationCodeFlowResponse generateAuthorizationCodeFlowData(
        @RequestBody OAuth2AuthorizationRequest authorizationRequest
    ) {
        final var codeChallengeAndVerifier = OAuth2Utils.generateSecretCodePair();
        final String state = OAuth2Utils.generateState();

        final String redirectUri = String.format(
            "http://%s:%s/login/oauth2/code/%s",
            authorizationRequest.clientHost(),
            authorizationRequest.clientPort(),
            authorizationRequest.clientId()
        );

        final String authorizationRequestUrl = String.format(
            "http://%s:%s/oauth2/authorize?" +
                "response_type=code" +
                "&client_id=%s" +
                "&redirect_uri=%s" +
                "&scope=%s" +
                "&code_challenge=%s" +
                "&code_challenge_method=S256" +
                "&state=%s",
            authorizationRequest.serverHost(),
            authorizationRequest.serverPort(),
            authorizationRequest.clientId(),

            redirectUri,
            authorizationRequest.scope(),

            codeChallengeAndVerifier.codeChallenge(),
            state
        );

        return new AuthorizationCodeFlowResponse(
            authorizationRequestUrl,
            codeChallengeAndVerifier.codeVerifier()
        );
    }

    @PostMapping("/token-request")
    public String generateTokenRequest(
        @RequestBody OAuth2TokenRequest tokenRequest
    ) {
        final String tokenRequestUrl = String.format(
            "http://%s:%s/oauth2/token",

            tokenRequest.serverHost(),
            tokenRequest.serverPort()
        );

        final String clientCredentials = Base64.getUrlEncoder().encodeToString(
            String.format(
                "%s:%s",
                tokenRequest.clientId(),
                tokenRequest.clientSecret()
            ).getBytes(StandardCharsets.UTF_8)
        );

        return String.format(
            """
            clear ; \\
            curl -sS -w '\\n%%{response_code}\\n' -X POST \\
                --data-urlencode 'client_id=%s' \\
                --data-urlencode 'redirect_uri=%s' \\
                --data-urlencode 'grant_type=authorization_code' \\
                --data-urlencode 'code=%s' \\
                --data-urlencode 'code_verifier=%s' \\
                --header 'Authorization: Basic %s' \\
                '%s'
            """,
            tokenRequest.clientId(),
            tokenRequest.redirectUri(),

            tokenRequest.code(),
            tokenRequest.codeVerifier(),

            clientCredentials,
            tokenRequestUrl
        );
    }
}