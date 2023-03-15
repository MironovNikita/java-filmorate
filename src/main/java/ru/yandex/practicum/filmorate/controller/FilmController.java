package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DateValidationException;
import ru.yandex.practicum.filmorate.exception.IncorrectPathDataException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
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
        if(film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895,12,
                28))) {
            log.warn("Фильм не может быть выпущен раньше дня рождения кино!");
            throw new DateValidationException("Фильм не может быть выпущен раньше дня рождения кино!");
        }
        filmService.getFilmStorage().create(film);
        log.info("Фильм '{}' с id '{}' был успешно добавлен.", film.getName(), film.getId());
        return film;
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@RequestBody @Valid Film film) {
        if(filmService.getFilmStorage().getById(film.getId()) == null) {
            log.warn("Запрос на обновление фильма с id '{}' отклонён. Он отсутствует в списке фильмов.", film.getId());
            throw new ObjectNotFoundException(film.getName(), film.getId());
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.warn("Фильм не может быть выпущен раньше дня рождения кино!");
            throw new DateValidationException("Фильм не может быть выпущен раньше дня рождения кино!");
        }
        filmService.getFilmStorage().update(film);
        log.info("Фильм '{}' с id '{}' был успешно обновлён.", film.getName(), film.getId());
        return film;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> showAllFilmsList() {
        log.info("Запрошен список всех фильмов.");
        return new ArrayList<>(filmService.getFilmStorage().getAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilm(@PathVariable Optional<Long> id) {
        if(id.isPresent()) {
            long correctId = id.get();
            if(filmService.getFilmStorage().getById(correctId) != null) {
                log.info("Запрошен фильм с id '{}' ", correctId);
                return filmService.getFilmStorage().getById(correctId);
            } else {
                log.warn("Фильм не найден. Передан отсутствующий id фильма");
                throw new ObjectNotFoundException("Фильм", correctId);
            }
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
            if(filmService.getFilmStorage().getById(correctId) != null) {
                log.info("Запрошено удаление фильма с id '{}'", correctId);
                return filmService.getFilmStorage().delete(correctId);
            } else {
                log.warn("Фильм не найден. Передан отсутствующий id фильма");
                throw new ObjectNotFoundException("Фильм", correctId);
            }
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
            Film film = filmService.getFilmStorage().getById(correctFilmId);
            User user = filmService.getUserStorage().getById(correctUserId);
            if(film != null && user != null) {
                log.info("Фильму с id '{}' был поставлен лайк :)", correctFilmId);
                return filmService.likeFilm(correctFilmId, correctUserId);
            } else {
                log.warn("Данные не найдены. Передан отсутствующий id пользователя/фильма.");
                if (film == null) {
                    throw new ObjectNotFoundException("Фильм", correctFilmId);
                } else {
                    throw new ObjectNotFoundException("Пользователь", correctUserId);
                }
            }
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
            Film film = filmService.getFilmStorage().getById(correctFilmId);
            User user = filmService.getUserStorage().getById(correctUserId);
            if(film != null && user != null) {
                log.info("У фильма с id '{}' был убран лайк :(", correctFilmId);
                return filmService.deleteLikeFromFilm(correctFilmId, correctUserId);
            } else {
                log.warn("Данные не найдены. Передан отсутствующий id пользователя/фильма.");
                if (film == null) {
                    throw new ObjectNotFoundException("Фильм", correctFilmId);
                } else {
                    throw new ObjectNotFoundException("Пользователь", correctUserId);
                }
            }
        } else {
            log.warn("Некорректные данные по id. Проверьте id фильма и пользователя.");
            throw new IncorrectPathDataException("Проверьте id фильма и пользователя. Данные некорректны.");
        }
    }
}
