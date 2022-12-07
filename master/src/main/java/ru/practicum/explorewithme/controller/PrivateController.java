package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.*;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.event.UpdateEventRequestDto;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.service.event.EventService;
import ru.practicum.explorewithme.service.publishers.PublisherService;
import ru.practicum.explorewithme.service.request.RequestService;
import ru.practicum.explorewithme.util.RequestBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
@Slf4j
public class PrivateController {

    private final EventService eventService;

    private final RequestService requestService;

    private final PublisherService publisherService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Positive @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return eventService.getEvents(userId, from, size);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @Valid @RequestBody UpdateEventRequestDto updateEventRequestDto,
                                    HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return eventService.updateEvent(userId, updateEventRequestDto);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @Valid @RequestBody NewEventDto newEventDto,
                                 HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId,
                                 HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable Long userId,
                                                          @PathVariable Long eventId,
                                                          HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return requestService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @PathVariable Long reqId,
                                                  HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return requestService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @PathVariable Long reqId,
                                                 HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return requestService.rejectRequest(userId, eventId, reqId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                     HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return requestService.getRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto addRequest(@PathVariable Long userId,
                                              @RequestParam Long eventId,
                                              HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId,
                                                 HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping("/{userId}/publishers")
    public List<UserDto> getPublishers(@PathVariable Long userId,
                                       HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return publisherService.getPublishers(userId);
    }

    @PostMapping("/{userId}/publishers/{publisherId}")
    public void addPublisher(@PathVariable Long userId,
                             @PathVariable Long publisherId,
                             HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        publisherService.addPublisher(userId, publisherId);
    }

    @DeleteMapping("/{userId}/publishers/{publisherId}")
    public void deletePublisher(@PathVariable Long userId,
                                @PathVariable Long publisherId,
                                HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        publisherService.deletePublisher(userId, publisherId);
    }

    @GetMapping("/{userId}/publishers/events")
    public List<EventShortDto> getAllPublisherEvents(@PathVariable Long userId,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                  @Positive @RequestParam(defaultValue = "10") int size,
                                                  HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return publisherService.getAllPublisherEvents(userId, from, size);
    }

    @GetMapping("/{userId}/publishers/events/{publisherId}")
    public List<EventShortDto> getPublisherEvents(@PathVariable Long userId,
                                                  @PathVariable Long publisherId,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                  @Positive @RequestParam(defaultValue = "10") int size,
                                                  HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return publisherService.getPublisherEvents(userId, publisherId, from, size);
    }

}
