package ru.practicum.explorewithme.dto.event;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explorewithme.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewEventDto {

    @NotNull
    @Size(min = 20, max = 2000, message = "Annotation must be between 20 and 2000 characters")
    private String annotation;

    @NotNull
    private Long category;

    @NotNull
    @Size(min = 20, max = 7000, message = "Annotation must be between 20 and 7000 characters")
    private String description;

    @NotNull
    private String eventDate;

    @NotNull
    private Location location;

    private boolean paid = false;

    private int participantLimit = 0;

    private boolean requestModeration = true;

    @NotNull
    @Size(min = 3, max = 120, message = "Annotation must be between 3 and 120 characters")
    private String title;
}
