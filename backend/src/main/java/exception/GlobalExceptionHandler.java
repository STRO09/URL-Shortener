package exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleIntegrityViolation(
            DataIntegrityViolationException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Custom alias already taken!");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}