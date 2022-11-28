package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.event.AdminUpdateEventRequestDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.user.NewUserRequestDto;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.service.category.CategoryService;
import ru.practicum.explorewithme.service.compilation.CompilationService;
import ru.practicum.explorewithme.service.event.EventService;
import ru.practicum.explorewithme.service.user.UserService;
import ru.practicum.explorewithme.util.RequestBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Validated
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final EventService eventService;

    private final CategoryService categoryService;

    private final UserService userService;

    private final CompilationService compilationService;

    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                        @Positive @RequestParam(defaultValue = "10") int size,
                                        HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @RequestBody AdminUpdateEventRequestDto requestDto,
                                    HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return eventService.updateEvent(eventId, requestDto);
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId,
                                     HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto publishReject(@PathVariable Long eventId,
                                      HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return eventService.rejectEvent(eventId);
    }

    @PatchMapping("/categories")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto,
                                      HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return categoryService.updateCategory(categoryDto);
    }

    @PostMapping("/categories")
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto,
                                   HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable Long catId,
                               HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        categoryService.deleteCategory(catId);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                  @Positive @RequestParam(defaultValue = "10") Integer size,
                                  HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    public UserDto addUser(@Valid @RequestBody NewUserRequestDto newUserRequestDto,
                           HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return userService.addUser(newUserRequestDto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId,
                           HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        userService.deleteUser(userId);
    }

    @PostMapping("/compilations")
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto,
                                         HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId,
                                  HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId,
                                           @PathVariable Long eventId,
                                           HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compId,
                                      @PathVariable Long eventId,
                                      HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId,
                                 HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        compilationService.unpinCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId,
                               HttpServletRequest request) {
        log.info("Get new request: {}", RequestBuilder.getStringFromRequest(request));
        compilationService.pinCompilation(compId);
    }
}




