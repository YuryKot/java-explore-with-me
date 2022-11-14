package ru.practicum.explorewithme.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dao.CompilationRepository;
import ru.practicum.explorewithme.dao.EventRepository;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.exception.EventNotFoundException;
import ru.practicum.explorewithme.mapper.CompilationMapper;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        Set<Event> events = newCompilationDto.getEvents()
                .stream()
                .map(id -> eventRepository.findById(id).orElseThrow(() -> new RuntimeException("")))
                .collect(Collectors.toSet());
        compilation.setEvents(events);
        compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        compilation.setEvents(compilation.getEvents()
                .stream()
                .filter(event -> !event.getId().equals(eventId))
                .collect(Collectors.toSet()));
    }

    @Override
    public void addEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        compilation.getEvents().add(event);
    }

    @Override
    public void unpinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        compilation.setPinned(false);
    }

    @Override
    public void pinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        compilation.setPinned(true);
    }
}
