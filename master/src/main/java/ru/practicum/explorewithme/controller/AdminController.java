package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.CategoryDto;
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
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users) {
        return null;
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @RequestBody AdminUpdateEventRequestDto requestDto) {
        return eventService.updateEvent(eventId, requestDto);
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto publishReject(@PathVariable Long eventId) {
        return eventService.publishReject(eventId);
    }

    @PatchMapping("/categories")
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @PostMapping("/categories")
    public CategoryDto addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    public UserDto addUser(@RequestBody NewUserRequestDto newUserRequestDto) {
        return userService.addUser(newUserRequestDto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("/compilations")
    public CompilationDto addCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId,
                                           @PathVariable Long eventId) {
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventFromCompilation(@PathVariable Long compId,
                                        @PathVariable Long eventId) {
        compilationService.addEventFromCompilation(compId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        compilationService.unpinCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        compilationService.pinCompilation(compId);
    }
}




