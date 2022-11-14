package ru.practicum.explorewithme.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}
