package com.epam.rd.autocode.spring.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(Model model) {
        return showError(model, HttpStatus.NOT_FOUND, "The requested resource was not found.");
    }

    @ExceptionHandler(AlreadyExistException.class)
    public String handleAlreadyExists(Model model) {
        return showError(model, HttpStatus.CONFLICT, "The request conflicts with existing data.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleInvalidRequest(IllegalArgumentException exception, Model model) {
        model.addAttribute("errorMessage", exception.getMessage());
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleUnexpectedError(Model model) {
        return showError(model, HttpStatus.INTERNAL_SERVER_ERROR,
                "The request could not be completed. Please try again later.");
    }

    private String showError(Model model, HttpStatus status, String errorMessage) {
        model.addAttribute("status", status.value());
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}
