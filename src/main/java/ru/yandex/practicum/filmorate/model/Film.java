package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    @NotNull
    @PositiveOrZero
    private Long id = 0L;
    @NotBlank(message = "Название фильма не может быть null")
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private double duration;
    private Set<Long> likes;
    @NotNull
    @PositiveOrZero
    private Integer rating = 0;
}
