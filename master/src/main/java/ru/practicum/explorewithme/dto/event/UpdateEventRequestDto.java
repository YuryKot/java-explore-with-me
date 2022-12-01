package ru.practicum.explorewithme.dto.event;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateEventRequestDto implements UpdateEventDto {

    @NotNull
    private Long eventId;

    @Size(min = 20, max = 2000, message = "Annotation must be between 20 and 2000 characters")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "Annotation must be between 20 and 7000 characters")
    private String description;

    private String eventDate;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 3, max = 120, message = "Annotation must be between 3 and 120 characters")
    private String title;
}
