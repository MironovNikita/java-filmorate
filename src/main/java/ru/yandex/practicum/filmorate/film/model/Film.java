package ru.yandex.practicum.filmorate.film.model;

import lombok.Value;
import lombok.With;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@With
@Value
public class Film {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    Mpa mpa;
    LinkedHashSet<Genre> genres;
}
