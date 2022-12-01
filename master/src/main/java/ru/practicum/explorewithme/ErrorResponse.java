package ru.practicum.explorewithme;

import lombok.Data;
import ru.practicum.explorewithme.mapper.DateTimeMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
public class ErrorResponse {
    private final List<String> errors;
    private final String message;
    private final String reason;
    private final String status;
    private final String timestamp;

    public ErrorResponse(List<String> errors, String message, String reason, String status) {
        this.errors = errors;
        this.message = message;
        this.reason = reason;
        this.status = status;
        timestamp = DateTimeMapper.toString(LocalDateTime.now());
    }

    public ErrorResponse(String error, String message, String reason, String status) {
        errors = Collections.singletonList(error);
        this.message = message;
        this.reason = reason;
        this.status = status;
        timestamp = DateTimeMapper.toString(LocalDateTime.now());
    }
}
