package ru.stud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.stud.dto.ErrorMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(ResourceNotFoundException e) {
        ErrorMessage error = new ErrorMessage(e.getErrorCode(), e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ObjectDeletionException.class)
    public ResponseEntity<ErrorMessage> handleDeletion(ObjectDeletionException e) {
        ErrorMessage error = new ErrorMessage(e.getErrorCode(), e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
