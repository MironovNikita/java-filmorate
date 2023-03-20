package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectPathDataException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@RequestBody @Valid Film film) {
        filmService.getFilmStorage().create(film);
        return film;
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@RequestBody @Valid Film film) {
        filmService.getFilmStorage().update(film);
        return film;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> showAllFilmsList() {
        return new ArrayList<>(filmService.getFilmStorage().getAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilm(@PathVariable Optional<Long> id) {
        if(id.isPresent()) {
            long correctId = id.get();
            return filmService.getFilmStorage().getById(correctId);
        } else {
            log.warn("Фильм не найден. Передан некорректный id фильма");
            throw new IncorrectPathDataException("Фильм не найден. Передан некорректный id фильма.");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film deleteFilm(@PathVariable Optional <Long> id) {
        if(id.isPresent()) {
            long correctId = id.get();
            return filmService.getFilmStorage().delete(correctId);
        } else {
            log.warn("Фильм не удалён. Передан некорректный id фильма.");
            throw new IncorrectPathDataException("Фильм не удалён. Передан некорректный id фильма.");
        }
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getMostLikedFilms(@RequestParam(defaultValue = "10") Optional <Integer> count) {
        if(count.isPresent()) {
            int correctCount = count.get();
            log.info("Запрошен список фильмов с наибольшим количеством лайков");
            return filmService.getMostLikedFilms(correctCount);
        } else {
            log.warn("Передано некорректное количество фильмов");
            throw new IncorrectPathDataException("Передано некорректное количество фильмов");
        }
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Film putLikeToFilm(@PathVariable("id") Optional <Long> filmId, @PathVariable Optional <Long> userId) {
        if(filmId.isPresent() && userId.isPresent()) {
            long correctFilmId = filmId.get();
            long correctUserId = userId.get();
            return filmService.likeFilm(correctFilmId, correctUserId);
        } else {
            log.warn("Некорректные данные по id. Проверьте id фильма и пользователя.");
            throw new IncorrectPathDataException("Проверьте id фильма и пользователя. Данные некорректны.");
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Film deleteLikeFromFilm(@PathVariable("id") Optional <Long> filmId, @PathVariable Optional <Long> userId) {
        if(filmId.isPresent() && userId.isPresent()) {
            long correctFilmId = filmId.get();
            long correctUserId = userId.get();
            return filmService.deleteLikeFromFilm(correctFilmId, correctUserId);
        } else {
            log.warn("Некорректные данные по id. Проверьте id фильма и пользователя.");
            throw new IncorrectPathDataException("Проверьте id фильма и пользователя. Данные некорректны.");
        }
    }
}
