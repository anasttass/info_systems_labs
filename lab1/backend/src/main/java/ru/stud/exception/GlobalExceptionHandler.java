package ru.stud.exception;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Объект не найден

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(
            ResourceNotFoundException e,
            Model model
    ) {

        model.addAttribute(
                "errorMessage",
                e.getMessage()
        );

        return "error";
    }


    // Невозможно удалить объект

    @ExceptionHandler(ObjectDeletionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDeletion(
            ObjectDeletionException e,
            Model model
    ) {

        model.addAttribute(
                "errorMessage",
                e.getMessage()
        );

        return "error";
    }
}