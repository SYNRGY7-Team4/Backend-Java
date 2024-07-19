package synrgy.team4.backend.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.dto.ApiResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> constraintViolationException(ConstraintViolationException exception) {
        log.error("ConstraintViolationException: {}", exception.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.<String>builder()
                        .success(false)
                        .errors(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<String>> apiException(ResponseStatusException exception) {
        log.error("ResponseStatusException: {}", exception.getReason());
        return ResponseEntity.status(exception.getStatusCode())
                .body(ApiResponse.<String>builder()
                        .success(false)
                        .errors(exception.getReason())
                        .build());
    }
}
