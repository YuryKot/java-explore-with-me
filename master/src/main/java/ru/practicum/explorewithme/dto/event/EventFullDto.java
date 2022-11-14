package ru.practicum.explorewithme.dto.event;

import lombok.Data;
import ru.practicum.explorewithme.dto.CategoryDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.model.Location;

@Data
public class EventFullDto {

    private Long id;

    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    private String createdOn;

    private String description;

    private String eventDate;

    private UserShortDto initiator;

    private Location location;

    private boolean paid;

    private int participantLimit;

    private String publishedOn;

    private boolean requestModeration;

    private String state;

    private String title;

    private int views;
}
