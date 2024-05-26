package me.iksadnorth.monitoring_server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor @Getter
public class MonitoringServerException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;
}
