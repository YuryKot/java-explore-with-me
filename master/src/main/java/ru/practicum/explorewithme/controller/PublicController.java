package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.client.StatisticClient;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.model.event.EndpointHit;
import ru.practicum.explorewithme.model.event.EventSort;
import ru.practicum.explorewithme.service.category.CategoryService;
import ru.practicum.explorewithme.service.compilation.CompilationService;
import ru.practicum.explorewithme.service.event.EventService;
import ru.practicum.explorewithme.util.RequestBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class PublicController {
    private final EventService eventService;

    private final CategoryService categoryService;

    private final StatisticClient statisticClient;

    private final CompilationService compilationService;

    @GetMapping("${events.path}")
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(defaultValue = "EVENT_DATE") EventSort sort,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        List<EventShortDto> eventShortDtos = eventService.getEventsWithFilter(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        statisticClient.postEndpointHit(new EndpointHit(request.getRequestURI(), request.getRemoteAddr()));
        return eventShortDtos;
    }

    @GetMapping("${events.path}" + "/{id}")
    public EventFullDto getEvent(@PathVariable Long id,
                                 HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        EventFullDto eventFullDto = eventService.getEvent(id);
        statisticClient.postEndpointHit(new EndpointHit(request.getRequestURI(), request.getRemoteAddr()));
        return eventFullDto;
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @Positive @RequestParam(defaultValue = "10") Integer size,
                                           HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId,
                                   HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return categoryService.getCategory(catId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size,
                                                HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId,
                                         HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return compilationService.getCompilation(compId);
    }
}
