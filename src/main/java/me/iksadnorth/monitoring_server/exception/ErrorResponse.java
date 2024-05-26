package me.iksadnorth.monitoring_server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public class ErrorResponse {
    private final String errorMessage;
}
