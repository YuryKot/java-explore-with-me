package ru.practicum.explorewithme.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "compilation")
@Data
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean pinned;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Event> events;
}
