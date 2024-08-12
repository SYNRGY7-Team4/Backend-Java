package synrgy.team4.backend.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.dto.response.BaseResponse;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<BaseResponse<String>> constraintViolationException(ConstraintViolationException exception) {
//        log.error("ConstraintViolationException: {}", exception.getMessage());
//        return ResponseEntity.badRequest()
//                .body(BaseResponse.<String>builder()
//                        .success(false)
//                        .errors(exception.getMessage())
//                        .build());
//    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<BaseResponse<String>> responseStatusException(ResponseStatusException exception) {
        log.error("ResponseStatusException: {}", exception.getReason());
        return ResponseEntity.status(exception.getStatusCode())
                .body(BaseResponse.<String>builder()
                        .success(false)
                        .errors(exception.getReason())
                        .build());
    }
  
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("Validation error: {}", errorMessage);
        return ResponseEntity.badRequest()
                .body(BaseResponse.<String>builder()
                        .success(false)
                        .errors(errorMessage)
                        .build());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        BaseResponse<Map<String, String>> response = BaseResponse.<Map<String, String>>builder()
                .success(false)
                .errors(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
