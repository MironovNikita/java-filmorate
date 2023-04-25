package ru.yandex.practicum.filmorate.film.dao;

import lombok.*;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@EqualsAndHashCode
@With
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FilmDao {
    @NotNull
    @PositiveOrZero
    private Long id = 0L;
    @NotBlank(message = "Название фильма не может быть null")
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    @NotNull
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;
}
