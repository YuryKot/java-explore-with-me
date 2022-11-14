package ru.practicum.explorewithme.mapper;

import lombok.AllArgsConstructor;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.model.Location;

import javax.validation.ValidationException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@AllArgsConstructor
public class EventMapper {



    public static Event toEvent(NewEventDto newEventDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(newEventDto.getDescription());
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), formatter);
        if (Duration.between(LocalDateTime.now(), eventDate).toHours() < 2) {
            throw new ValidationException("The event is less than two hours away");
        }
        event.setEventDate(eventDate);
        event.setLocationLat(newEventDto.getLocation().getLat());
        event.setLocationLon(newEventDto.getLocation().getLon());
        event.setPaid(newEventDto.isPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setState(EventState.PENDING);
        event.setTitle(newEventDto.getTitle());
        return event;
    }

    public static EventFullDto toEventFullDto(Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setCreatedOn(formatter.format(event.getCreatedOn()));
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setLocation(new Location(event.getLocationLat(), event.getLocationLon()));
        eventFullDto.setPaid(event.isPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(formatter.format(event.getPublishedOn()));
        eventFullDto.setRequestModeration(event.isRequestModeration());
        eventFullDto.setState(event.getState().toString());
        eventFullDto.setTitle(event.getTitle());
        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setEventDate(formatter.format(event.getEventDate()));
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.isPaid());
        eventShortDto.setTitle(event.getTitle());
        return eventShortDto;
    }
}
