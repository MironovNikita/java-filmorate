package ru.yandex.practicum.filmorate.film.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.common.exception.DateValidationException;
import ru.yandex.practicum.filmorate.common.exception.IncorrectPathDataException;
import ru.yandex.practicum.filmorate.film.dao.FilmDao;
import ru.yandex.practicum.filmorate.film.service.FilmService;
import ru.yandex.practicum.filmorate.film.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@RequestBody @Valid FilmDao filmDao) {
        log.info("Выполнение команды контроллера на создание фильма " + filmDao);
        checkFilmReleaseDate(filmDao.getReleaseDate());
        return filmService.create(filmDao);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@RequestBody @Valid FilmDao filmDao) {
        log.info("Выполнение команды контроллера на обновление фильма " + filmDao);
        checkFilmReleaseDate(filmDao.getReleaseDate());
        return filmService.update(filmDao);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> showAllFilmsList() {
        log.info("Выполнение команды контроллера на получение списка всех фильмов");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilm(@PathVariable Optional<Long> id) {
        if(id.isPresent()) {
            long correctId = id.get();
            log.info("Выполнение команды контроллера на получение фильма по id '{}'", correctId);
            return filmService.getById(correctId);
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
            log.info("Выполнение команды контроллера на удаление фильма по id '{}'", correctId);
            return filmService.delete(correctId);
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
    public void putLikeToFilm(@PathVariable("id") Optional <Long> filmId, @PathVariable Optional <Long> userId) {
        if(filmId.isPresent() && userId.isPresent()) {
            long correctFilmId = filmId.get();
            long correctUserId = userId.get();
            log.info("Фильму с id '{}' был поставлен лайк пользователем с id '{}'", correctFilmId, correctUserId);
            filmService.likeFilm(correctFilmId, correctUserId);
        } else {
            log.warn("Некорректные данные по id. Проверьте id фильма и пользователя.");
            throw new IncorrectPathDataException("Проверьте id фильма и пользователя. Данные некорректны.");
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLikeFromFilm(@PathVariable("id") Optional <Long> filmId, @PathVariable Optional <Long> userId) {
        if(filmId.isPresent() && userId.isPresent()) {
            long correctFilmId = filmId.get();
            long correctUserId = userId.get();
            log.info("У фильма с id '{}' был убран лайк пользователем с id '{}'", correctFilmId, correctUserId);
            filmService.deleteLikeFromFilm(correctFilmId, correctUserId);
        } else {
            log.warn("Некорректные данные по id. Проверьте id фильма и пользователя.");
            throw new IncorrectPathDataException("Проверьте id фильма и пользователя. Данные некорректны.");
        }
    }

    private void checkFilmReleaseDate(LocalDate date) {
        if (date.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new DateValidationException("Дата выпуска фильма не должна быть до 1895-12-28");
        }
    }
}
