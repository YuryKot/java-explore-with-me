package ru.practicum.explorewithme;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.exception.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({CategoryNotFoundException.class, CompilationNotFoundException.class,
    EventNotFoundException.class, RequestNotFoundExceprion.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final RuntimeException e) {
        return new ErrorResponse(List.of(e.getStackTrace()), e.getMessage(), null, HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        return new ErrorResponse(Arrays.asList(e.getStackTrace()), e.getMessage(), e.getConstraintName(), HttpStatus.CONFLICT.toString(),
                LocalDateTime.now().toString());
    }
}
