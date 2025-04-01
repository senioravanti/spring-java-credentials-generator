package ru.manannikov.credentialsgenerator.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String title;
    private int status;
    private String detail;

    private final Map<String, Object> properties = new HashMap<>();

    public static ErrorResponse forStatusAndDetail(
        HttpStatus status, @Nullable String detail
    ) {
        return ErrorResponse.builder()
            .status(status.value())
            .detail(detail)
        .build();
    }
}
