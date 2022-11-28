package ru.practicum.explorewithme.service.event;

import com.querydsl.core.BooleanBuilder;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.client.StatisticClient;
import ru.practicum.explorewithme.dao.CategoryRepository;
import ru.practicum.explorewithme.dao.EventRepository;
import ru.practicum.explorewithme.dao.UserRepository;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.exception.EventNotFoundException;
import ru.practicum.explorewithme.exception.UpdateImpossibleException;
import ru.practicum.explorewithme.exception.UserNotFoundException;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.model.*;
import ru.practicum.explorewithme.model.event.*;

import javax.persistence.EntityManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EntityManager entityManager;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final StatisticClient statisticClient;
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User initiator = findUser(userId);
        Category category = findCategory(newEventDto.getCategory());
        Event event = EventMapper.toEvent(newEventDto);
        event.setCategory(category);
        event.setInitiator(initiator);
        eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(0);
        eventFullDto.setViews(0);
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, int from, int size) {
        findUser(userId);
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<Event> events = eventRepository.findByInitiator_Id(userId, pageable);
        List<EventShortDto> eventShortDtoList =
                events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        setViews(eventShortDtoList);
        setConfirmedRequests(eventShortDtoList);
        return eventShortDtoList;
    }

    @Override
    public EventFullDto updateEvent(Long userId, UpdateEventRequestDto updateEventRequestDto) {
        findUser(userId);
        Event event = findEvent(updateEventRequestDto.getEventId());
        if (!event.getInitiator().getId().equals(userId)) {
            throw new UpdateImpossibleException("User with id=%d can`t update event with id=%d");
        }
        if (event.getState() != EventState.CANCELED && event.getState() != EventState.PENDING) {
            throw new UpdateImpossibleException("Event state for update must be CANCELED or PENDING");
        }
        if (Duration.between(LocalDateTime.now(), event.getEventDate()).toHours() < 2) {
            throw new UpdateImpossibleException("The events is less than two hours away");
        }

        updateEvent(event, updateEventRequestDto);

        if (updateEventRequestDto.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEventRequestDto.getEventDate(), formatter);
            if (Duration.between(LocalDateTime.now(), eventDate).toHours() < 2) {
                throw new UpdateImpossibleException("The update event is less than two hours away");
            }
            event.setEventDate(eventDate);
        }
        event.setState(EventState.PENDING);
        return getEventFullDto(event);
    }

    @Override
    public EventFullDto updateEvent(Long eventId, AdminUpdateEventRequestDto requestDto) {
        Event event = findEvent(eventId);
        updateEvent(event, requestDto);
        if (requestDto.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(requestDto.getEventDate(), formatter);
            event.setEventDate(eventDate);
        }
        if (requestDto.getLocation() != null) {
            event.setLocationLat(requestDto.getLocation().getLat());
            event.setLocationLon(requestDto.getLocation().getLon());
        }
        return getEventFullDto(event);
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        findUser(userId);
        Event event = findEvent(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new EventNotFoundException(eventId);
        }
        return getEventFullDto(event);
    }

    @Override
    public EventFullDto getEvent(Long eventId) {
        Event event = findEvent(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            throw new EventNotFoundException(eventId);
        }
        return getEventFullDto(event);
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        findUser(userId);
        Event event = findEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new UpdateImpossibleException("User with id=%d can`t update event with id=%d");
        }
        if (event.getState() != EventState.PENDING) {
            throw new UpdateImpossibleException("Only pending or canceled events can be changed");
        }
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return getEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEventsWithFilter(String text, List<Long> categories, Boolean paid,
                                                   String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                   EventSort sort, Integer from, Integer size) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QEvent qEvent = QEvent.event;
        QRequest qRequest = QRequest.request;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (text != null) {
            booleanBuilder.and((qEvent.annotation.likeIgnoreCase(text))
                    .or(qEvent.description.likeIgnoreCase(text)));
        }
        if (categories != null) {
            booleanBuilder.and(qEvent.category.id.in(categories));
        }
        if (paid != null) {
            booleanBuilder.and(qEvent.paid.eq(paid));
        }
        if (rangeStart == null && rangeEnd == null) {
            booleanBuilder.and(qEvent.eventDate.after(LocalDateTime.now()));
        } else {
            if (rangeStart != null) {
                booleanBuilder.and(qEvent.eventDate.after(LocalDateTime.parse(rangeStart, formatter)));
            }
            if (rangeEnd != null) {
                booleanBuilder.and(qEvent.eventDate.before(LocalDateTime.parse(rangeEnd, formatter)));
            }
        }
        booleanBuilder.and(qEvent.state.eq(EventState.PUBLISHED));
        booleanBuilder.and((qRequest.status.eq(StatusRequest.CONFIRMED).or(qRequest.isNull())));

        List<EventShortDto> eventsDto;
        if (sort == EventSort.EVENT_DATE) {
            List<Tuple> events = getEventsWithEventsDateSort(onlyAvailable, queryFactory, qEvent, qRequest,
                    booleanBuilder, from, size);
            eventsDto = mapListEventsToEventShortDto(events, qEvent, qRequest);
            setViews(eventsDto);
        } else {
            eventsDto = getEventsWithViewsSort(onlyAvailable, queryFactory, qEvent, qRequest, booleanBuilder, from, size);
        }
        return eventsDto;
    }


    @Override
    public EventFullDto publishEvent(Long eventId) {
        Event event = findEvent(eventId);
        if (Duration.between(LocalDateTime.now(), event.getEventDate()).toHours() < 1) {
            throw new UpdateImpossibleException("Event is less than one hour away");
        }
        if (event.getState() != EventState.PENDING) {
            throw new UpdateImpossibleException("Event must be in PENDING state");
        }
        event.setState(EventState.PUBLISHED);
        eventRepository.save(event);
        return getEventFullDto(event);
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        Event event = findEvent(eventId);
        if (event.getState() == EventState.PUBLISHED) {
            throw new UpdateImpossibleException("Event don`t must be in PUBLISHED state");
        }
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return getEventFullDto(event);
    }

    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<String> states, List<Long> categories, String rangeStart,
                                        String rangeEnd, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        QEvent qEvent = QEvent.event;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (users != null) {
            booleanBuilder.and(qEvent.initiator.id.in(users));
        }
        if (states != null) {
            booleanBuilder.and(qEvent.state.in(states.stream().map(EventState::valueOf).collect(Collectors.toList())));
        }
        if (categories != null) {
            booleanBuilder.and(qEvent.category.id.in(categories));
        }
        if (rangeStart != null) {
            booleanBuilder.and(qEvent.eventDate.after(LocalDateTime.parse(rangeStart, formatter)));
        }
        if (rangeEnd != null) {
            booleanBuilder.and(qEvent.eventDate.before(LocalDateTime.parse(rangeEnd, formatter)));
        }
        Iterable<Event> events = eventRepository.findAll(booleanBuilder, pageable);
        List<EventFullDto> eventFullDtoList = StreamSupport
                .stream(events.spliterator(), false)
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
        setViews(eventFullDtoList);
        setConfirmedRequests(eventFullDtoList);
        return eventFullDtoList;
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    private Event findEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    private void setViews(List<? extends EventDto> eventDtoList) {
        List<String> urisList = eventDtoList
                .stream()
                .map(e -> String.format("/events/%d", e.getId()))
                .collect(Collectors.toList());

        List<ViewStats> viewStatsList = statisticClient.getStatsByUris(urisList);

        for (EventDto eventsDto : eventDtoList) {
            eventsDto.setViews(
                    viewStatsList.stream()
                            .filter(v -> v.getUri().equals(
                                    String.format("/events/%d", eventsDto.getId())))
                            .findFirst()
                            .orElse(new ViewStats())
                            .getHits());
        }
    }

    private void setConfirmedRequests(List<? extends EventDto> eventDtoList) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QRequest qRequest = QRequest.request;

        List<Long> idList = eventDtoList
                .stream()
                .map(EventDto::getId)
                .collect(Collectors.toList());

        List<Tuple> tupleList = queryFactory.select(qRequest.event.id, qRequest.count())
                .from(qRequest)
                .where(qRequest.event.id.in(idList)
                        .and(qRequest.status.eq(StatusRequest.CONFIRMED)))
                .groupBy(qRequest.event)
                .fetch();

        for (EventDto eventDto : eventDtoList) {
            Optional<Tuple> tupleOptional = tupleList
                    .stream()
                    .filter(t -> t.get(qRequest.event.id).equals(eventDto.getId()))
                    .findFirst();
            if (tupleOptional.isPresent()) {
                eventDto.setConfirmedRequests(tupleOptional.get().get(qRequest.count()).intValue());
            } else {
                eventDto.setConfirmedRequests(0);
            }
        }
    }

    private EventFullDto getEventFullDto(Event event) {
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        setViews(Collections.singletonList(eventFullDto));
        setConfirmedRequests(Collections.singletonList(eventFullDto));
        return eventFullDto;
    }

    private <T extends UpdateEventDto> void updateEvent(Event event, T updateEventDto) {
        if (updateEventDto.getAnnotation() != null) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }
        if (updateEventDto.getCategory() != null) {
            Category category = findCategory(updateEventDto.getCategory());
            event.setCategory(category);
        }
        if (updateEventDto.getDescription() != null) {
            event.setDescription(updateEventDto.getDescription());
        }
        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }
        if (updateEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }
        if (updateEventDto.getTitle() != null) {
            event.setTitle(updateEventDto.getTitle());
        }
    }

    private <T> List<T> getSubList(List<T> list, int from, int size) {
        if (from + size > list.size()) {
            return new ArrayList<>();
        } else {
            return list.subList(from, from + size);
        }
    }

    private List<Tuple> getEventsWithEventsDateSort(Boolean onlyAvailable, JPAQueryFactory queryFactory,
                                                    QEvent qEvent, QRequest qRequest, BooleanBuilder booleanBuilder,
                                                    int from, int size) {
        if (onlyAvailable) {
            return queryFactory.select(qEvent, qRequest.count())
                    .from(qEvent)
                    .leftJoin(qRequest).on(qEvent.eq(qRequest.event))
                    .where(booleanBuilder)
                    .groupBy(qEvent)
                    .having(qEvent.participantLimit.eq(0)
                            .or(qEvent.participantLimit.gt(qRequest.count())))
                    .orderBy(qEvent.eventDate.asc())
                    .limit(size)
                    .offset(from)
                    .fetch();
        } else {
            return queryFactory.select(qEvent, qRequest.count())
                    .from(qEvent)
                    .leftJoin(qRequest).on(qEvent.eq(qRequest.event))
                    .fetchJoin()
                    .where(booleanBuilder)
                    .groupBy(qEvent)
                    .orderBy(qEvent.eventDate.asc())
                    .limit(size)
                    .offset(from)
                    .fetch();
        }
    }

    private List<EventShortDto> getEventsWithViewsSort(Boolean onlyAvailable, JPAQueryFactory queryFactory,
                                                       QEvent qEvent, QRequest qRequest, BooleanBuilder booleanBuilder,
                                                       int from, int size) {
        List<Tuple> tupleList;
        List<EventShortDto> eventsDto;
        if (onlyAvailable) {
            tupleList = queryFactory.select(qEvent, qRequest.count())
                    .from(qEvent)
                    .leftJoin(qRequest).on(qEvent.eq(qRequest.event))
                    .where(booleanBuilder)
                    .groupBy(qEvent)
                    .having(qEvent.participantLimit.eq(0)
                            .or(qEvent.participantLimit.gt(qRequest.count())))
                    .orderBy(qEvent.eventDate.asc())
                    .fetch();
        } else {
            tupleList = queryFactory.select(qEvent, qRequest.count())
                    .from(qEvent)
                    .leftJoin(qRequest).on(qEvent.eq(qRequest.event))
                    .fetchJoin()
                    .where(booleanBuilder)
                    .groupBy(qEvent)
                    .orderBy(qEvent.eventDate.asc())
                    .fetch();
        }
        eventsDto = mapListEventsToEventShortDto(tupleList, qEvent, qRequest);
        setViews(eventsDto);
        eventsDto.sort((o1, o2) -> (-Integer.compare(o1.getViews(), o2.getViews())));
        return getSubList(eventsDto, from, size);
    }

    private List<EventShortDto> mapListEventsToEventShortDto(List<Tuple> tupleList, QEvent qEvent, QRequest qRequest) {
        return tupleList.stream()
                .map(e -> {
                    EventShortDto eventShortDto = EventMapper.toEventShortDto(e.get(qEvent));
                    eventShortDto.setConfirmedRequests(e.get(qRequest.count()).intValue());
                    return eventShortDto;
                })
                .collect(Collectors.toList());
    }
}
