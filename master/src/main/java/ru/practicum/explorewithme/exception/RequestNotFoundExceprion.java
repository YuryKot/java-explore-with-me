package ru.practicum.explorewithme.exception;

public class RequestNotFoundExceprion extends RuntimeException{

    public RequestNotFoundExceprion(Long id) {
        super(String.format("Request with id=%d not found", id));
    }
}
