package ru.practicum.explorewithme.dto.event;

import lombok.Data;
import ru.practicum.explorewithme.dto.CategoryDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.model.Location;

@Data
public class EventShortDto {

    private  Long id;

    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    private String eventDate;

    private UserShortDto initiator;

    private Location location;

    private boolean paid;

    private String title;

    private int views;
}
