package ru.yandex.practicum.filmorate.serviceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidateFilmService;

import java.time.LocalDate;

public class ValidateFilmServiceTest {
    @DisplayName("Проверка создания фильма с пустым названием")
    @Test
    void shouldThrowValidateExceptionIfFilmNameIsBlank() {
        ValidateFilmService validateService = new ValidateFilmService();
        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(1990, 10,
                22));
        film.setDuration(120);
        film.setName("           ");
        Assertions.assertThrows(ValidationException.class, () -> validateService.validateFilm(film));
    }

    @DisplayName("Проверка длины описания фильма")
    @Test
    void shouldThrowValidateExceptionIfFilmDescriptionIsMore200Symbols() {
        ValidateFilmService validateService = new ValidateFilmService();
        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(1990, 10,
                22));
        film.setDuration(120);
        film.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod" +
                "tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud" +
                "exerci tation ullamcor");
        Assertions.assertThrows(ValidationException.class, () -> validateService.validateFilm(film));
    }

    @DisplayName("Проверка даты релиза фильма")
    @Test
    void shouldThrowValidateExceptionIfFilmReleaseDateIsBefore18951228() {
        ValidateFilmService validateService = new ValidateFilmService();
        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(990, 10,
                22));
        film.setDuration(120);
        Assertions.assertThrows(ValidationException.class, () -> validateService.validateFilm(film));
    }

    @DisplayName("Проверка продолжительности фильма")
    @Test
    void shouldThrowValidateExceptionIfFilmHasIncorrectDuration() {
        ValidateFilmService validateService = new ValidateFilmService();
        Film film = new Film("Фильм", "Описание фильма", LocalDate.of(1990, 10,
                22));
        film.setDuration(0);
        Assertions.assertThrows(ValidationException.class, () -> validateService.validateFilm(film));
        film.setDuration(-10);
        Assertions.assertThrows(ValidationException.class, () -> validateService.validateFilm(film));
    }
}
