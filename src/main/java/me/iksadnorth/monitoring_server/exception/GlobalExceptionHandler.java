package me.iksadnorth.monitoring_server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MonitoringServerException.class)
    public ResponseEntity<ErrorResponse> handleMonitoringServerException(MonitoringServerException e) {
        String message = e.getMessage();

        log.warn(message);

        ErrorResponse errorResponse = new ErrorResponse(message);
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }
}
