package ru.practicum.explorewithme.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dao.CategoryRepository;
import ru.practicum.explorewithme.dao.EventRepository;
import ru.practicum.explorewithme.dao.UserRepository;
import ru.practicum.explorewithme.dto.*;
import ru.practicum.explorewithme.dto.event.AdminUpdateEventRequestDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.exception.EventNotFoundException;
import ru.practicum.explorewithme.exception.UpdateImpossibleException;
import ru.practicum.explorewithme.exception.UserNotFoundException;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User initiator = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(newEventDto.getCategory()));
        Event event = EventMapper.toEvent(newEventDto);
        event.setCategory(category);
        event.setInitiator(initiator);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        eventFullDto.setConfirmedRequests(0);
        eventFullDto.setViews(0);
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, int from, int size) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<Event> events = eventRepository.findByInitiator_Id(userId, pageable);
        return events.stream().map(
                event -> {
                    EventShortDto eventShortDto = EventMapper.toEventShortDto(event);
                    eventShortDto.setConfirmedRequests(0);
                    eventShortDto.setViews(0);
                    return eventShortDto;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(Long userId, UpdateEventRequestDto updateEventRequestDto) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(updateEventRequestDto.getId())
                .orElseThrow(() -> new EventNotFoundException(updateEventRequestDto.getId()));
        if (event.getInitiator().getId().equals(userId)) {
            throw new UpdateImpossibleException("User with id=%d can`t update event with id=%d");
        }
        if (event.getState() != EventState.CANCELED || event.getState() != EventState.PENDING) {
            throw new UpdateImpossibleException("Event state for update must be CANCELED or PENDING");
        }
        if (Duration.between(LocalDateTime.now(), event.getEventDate()).toHours() < 2) {
            throw new UpdateImpossibleException("The events is less than two hours away");
        }
        if (updateEventRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventRequestDto.getAnnotation());
        }
        if (updateEventRequestDto.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventRequestDto.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException(updateEventRequestDto.getCategory()));
            event.setCategory(category);
        }
        if (updateEventRequestDto.getDescription() != null) {
            event.setDescription(updateEventRequestDto.getDescription());
        }
        if (updateEventRequestDto.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEventRequestDto.getEventDate(), formatter);
            if (Duration.between(LocalDateTime.now(), eventDate).toHours() < 2) {
                throw new UpdateImpossibleException("The update event is less than two hours away");
            }
            event.setEventDate(eventDate);
        }
        if (updateEventRequestDto.getPaid() != null) {
            event.setPaid(updateEventRequestDto.getPaid());
        }
        if (updateEventRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequestDto.getParticipantLimit());
        }
        if (updateEventRequestDto.getTitle() != null) {
            event.setTitle(updateEventRequestDto.getTitle());
        }
        event.setState(EventState.PENDING);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(0);
        eventFullDto.setViews(0);
        return eventFullDto;
    }

    @Override
    public EventFullDto updateEvent(Long eventId, AdminUpdateEventRequestDto requestDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (requestDto.getAnnotation() != null) {
            event.setAnnotation(requestDto.getAnnotation());
        }
        if (requestDto.getCategory() != null) {
            Category category = categoryRepository.findById(requestDto.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException(requestDto.getCategory()));
            event.setCategory(category);
        }
        if (requestDto.getDescription() != null) {
            event.setDescription(requestDto.getDescription());
        }
        if (requestDto.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(requestDto.getEventDate(), formatter);
            if (Duration.between(LocalDateTime.now(), eventDate).toHours() < 2) {
                throw new UpdateImpossibleException("The update event is less than two hours away");
            }
            event.setEventDate(eventDate);
        }
        if (requestDto.getLocation() != null) {
            event.setLocationLat(requestDto.getLocation().getLat());
            event.setLocationLon(requestDto.getLocation().getLon());
        }
        if (requestDto.getPaid() != null) {
            event.setPaid(requestDto.getPaid());
        }
        if (requestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(requestDto.getParticipantLimit());
        }
        if (requestDto.getTitle() != null) {
            event.setTitle(requestDto.getTitle());
        }
        event.setState(EventState.PENDING);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(0);
        eventFullDto.setViews(0);
        return eventFullDto;
    }

    @Override
    public EventFullDto getEvent(/*Long userId, */Long eventId) {
        /*userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId));*/
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(0);
        eventFullDto.setViews(0);
        return eventFullDto;
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new UpdateImpossibleException("User with id=%d can`t update event with id=%d");
        }
        if (event.getState() != EventState.PENDING) {
            throw new UpdateImpossibleException("Only pending or canceled events can be changed");
        }
        event.setState(EventState.CANCELED);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(0);
        eventFullDto.setViews(0);
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> getEventsWithFilter(String text, List<Integer> categories, Boolean paid,
                                                   String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                   EventSort sort, Integer from, Integer size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        LocalDateTime start;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, formatter);
        } else {
            start = LocalDateTime.now();
        }
        LocalDateTime end;
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, formatter);
        } else {
            end = LocalDateTime.MAX;
        }
        List<Event> events = eventRepository.getEventsWithFilter(text, categories, paid, start, end,
                EventState.PUBLISHED, pageable);
        List<EventShortDto> eventShortDtos = events.stream().map(
                        event -> {
                            EventShortDto eventShortDto = EventMapper.toEventShortDto(event);
                            eventShortDto.setConfirmedRequests(0);
                            eventShortDto.setViews(0);
                            if (onlyAvailable && eventShortDto.getConfirmedRequests() >= event.getParticipantLimit()) {
                                return null;
                            }
                            return eventShortDto;
                        }
                ).filter(e -> e != null)
                .collect(Collectors.toList());
        return eventShortDtos;
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (Duration.between(LocalDateTime.now(), event.getEventDate()).toHours() < 1) {
            throw new UpdateImpossibleException("Event is less than one hour away");
        }
        if (event.getState() != EventState.PENDING) {
            throw new UpdateImpossibleException("Event must be in PENDING state");
        }
        event.setState(EventState.PUBLISHED);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(0);
        eventFullDto.setViews(0);
        return eventFullDto;
    }

    @Override
    public EventFullDto publishReject(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getState() == EventState.PUBLISHED) {
            throw new UpdateImpossibleException("Event don`t must be in PUBLISHED state");
        }
        event.setState(EventState.CANCELED);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(0);
        eventFullDto.setViews(0);
        return eventFullDto;
    }
}
