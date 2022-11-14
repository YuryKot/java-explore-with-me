package ru.practicum.explorewithme.dto.event;

import lombok.Data;
import ru.practicum.explorewithme.model.Location;

@Data
public class AdminUpdateEventRequestDto {

    private String annotation;

    private Long category;

    private String description;

    private String eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private String title;
}
