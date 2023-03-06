package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.data.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidateFilmService;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final ValidateFilmService filmValidateService = new ValidateFilmService();
    private final FilmRepository filmRepository = new FilmRepository();

    @PostMapping()
    public Film create(@RequestBody Film film) {
        film = filmValidateService.validateFilm(film);
        filmRepository.saveFilm(film);
        log.info("Фильм '{}' с id '{}' был успешно добавлен.", film.getName(), film.getId());
        return film;
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        if(!filmRepository.getFilms().containsKey(film.getId())) {
            log.warn("Запрос на обновление фильма с id '{}' отклонён. Он отсутствует в списке фильмов.", film.getId());
            throw new RuntimeException("Список фильмов не содержит такого ID");
        }
        film = filmValidateService.validateFilm(film);
        filmRepository.getFilms().put(film.getId(), film);
        log.info("Фильм '{}' с id '{}' был успешно обновлён.", film.getName(), film.getId());
        return film;
    }

    @GetMapping
    public Collection<Film> showAllFilmsList() {
        log.info("Запрошен список всех фильмов.");
        return new ArrayList<>(filmRepository.getFilms().values());
    }
}
