package ru.practicum.explorewithme.exception;

public class PublisherNotFoundException extends RuntimeException {

    public PublisherNotFoundException(Long userId, Long publisherId) {
        super(String.format("User with id=%d don`t have publisher with id=%d", userId, publisherId));
    }
}
