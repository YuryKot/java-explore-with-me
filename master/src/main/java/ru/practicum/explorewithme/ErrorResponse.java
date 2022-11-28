package ru.practicum.explorewithme;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    private final List<StackTraceElement> errors;
    private final String message;
    private final String reason;
    private final String status;
    private final String timestamp;
}
