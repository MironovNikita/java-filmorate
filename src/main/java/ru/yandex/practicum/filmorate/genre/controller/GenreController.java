package ru.yandex.practicum.filmorate.genre.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.common.exception.IncorrectPathDataException;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.genre.service.GenreService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/{id}")
    public Genre getById(@PathVariable ("id") Optional<Integer> genreId) {
        if(genreId.isPresent()) {
            int correctId = genreId.get();
            log.info("Выполнение команды контроллера на получение жанра фильма с id '{}'", correctId);
            return genreService.getById(correctId);
        } else {
            log.warn("Жанр не обнаружен. Некорректный id жанра.");
            throw new IncorrectPathDataException("Жанр не обнаружен. Некорректный id жанра.");
        }
    }

    @GetMapping
    public List<Genre> getAll() {
        log.info("Выполнение команды контроллера на получение списка всех жанров фильмов");
        return genreService.getAll();
    }
}
