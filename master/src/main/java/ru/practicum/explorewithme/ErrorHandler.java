package ru.practicum.explorewithme;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.exception.*;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({CategoryNotFoundException.class, CompilationNotFoundException.class,
    EventNotFoundException.class, RequestNotFoundExceprion.class, UserNotFoundException.class,
    PublisherNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final RuntimeException e) {
        log.info(HttpStatus.BAD_REQUEST + ": " + e.getMessage());
        return new ErrorResponse(e.getClass().toString(), e.getMessage(), "Element not found", HttpStatus.NOT_FOUND.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        log.info(HttpStatus.CONFLICT + ": " + e.getMessage());
        return new ErrorResponse(e.getConstraintName(), e.getMessage(), "Constraint exception", HttpStatus.CONFLICT.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingParameterException(final MissingServletRequestParameterException e) {
        log.info(HttpStatus.BAD_REQUEST + ": " + e.getMessage());
        return new ErrorResponse(e.getParameterName() + " parameter is missing", e.getMessage(),
                "Required parameter not found", HttpStatus.BAD_REQUEST.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.info(HttpStatus.BAD_REQUEST + ": " + e.getMessage());
        List<String> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return new ErrorResponse(errors, e.getFieldErrors().toString(), "Not Valid parameter", HttpStatus.BAD_REQUEST.toString());
    }
}
