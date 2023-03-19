package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void checkFilmLikesList(Film film) {
        if(film.getLikes() == null) {
            film.setLikes(new HashSet<Long>());
        }
    }
    public Film likeFilm(long filmId, long userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        if(film == null) {
            log.warn("Данные не найдены. Передан отсутствующий id фильма.");
            throw new ObjectNotFoundException("Фильм", filmId);
        }
        if(user == null) {
            log.warn("Данные не найдены. Передан отсутствующий id пользователя.");
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        checkFilmLikesList(film);
        film.getLikes().add(userId);
        film.setRating(film.getRating() + 1);
        log.info("Фильму с id '{}' был поставлен лайк :)", filmId);
        return filmStorage.update(film);
    }

    public Film deleteLikeFromFilm(long filmId, long userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        if(film == null) {
            log.warn("Данные не найдены. Передан отсутствующий id фильма.");
            throw new ObjectNotFoundException("Фильм", filmId);
        }
        if(user == null) {
            log.warn("Данные не найдены. Передан отсутствующий id пользователя.");
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        checkFilmLikesList(film);
        film.getLikes().remove(userId);
        film.setRating(film.getRating() - 1);
        log.info("У фильма с id '{}' был убран лайк :(", filmId);
        return filmStorage.update(film);
    }

    public List<Film> getMostLikedFilms(int quantity) {
        List<Film> popularFilms = filmStorage.getAll();
        for(Film film : popularFilms) checkFilmLikesList(film);
        return popularFilms.stream()
                .sorted((film1, film2) -> film2.getRating() - film1.getRating())
                .limit(Math.max(quantity, 0)).collect(Collectors.toList());
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }
}
