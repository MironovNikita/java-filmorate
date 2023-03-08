package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private long id;
    @NotBlank
    @NotNull(message = "Имя не может быть null")
    private String name;
    @NotBlank
    @Size(max = 200)
    @NotNull(message = "Описание не может быть null")
    private String description;
    @NotNull(message = "Дата релиза не может быть null")
    private LocalDate releaseDate;
    @Positive
    private double duration;
}
