package ru.manannikov.credentialsgenerator.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.manannikov.credentialsgenerator.dto.ErrorResponse;
import org.springframework.lang.Nullable;

import java.util.Optional;


@Slf4j
@RestControllerAdvice
public class RestErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("bad request:\nexception: {}", ex.toString());

        return createDefaultProblemDetail(
            HttpStatus.BAD_REQUEST,
            "Некорректные параметры запроса",
            Optional.of(ex.getMessage())
        );

    }

    private ErrorResponse createDefaultProblemDetail(
        HttpStatus statusCode,
        String detail,
        Optional<String> description
    ) {
        final var problemDetail = ErrorResponse.forStatusAndDetail(
            statusCode,
            detail
        );

        if (description.isPresent()) {
            problemDetail.getProperties().put("description", description);
        }
        return problemDetail;
    }
}
