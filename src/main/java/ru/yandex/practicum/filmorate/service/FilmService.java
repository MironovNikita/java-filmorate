package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
            throw new ObjectNotFoundException("Фильм", filmId);
        }
        if(user == null) {
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        checkFilmLikesList(film);
        film.getLikes().add(userId);
        return filmStorage.update(film);
    }

    public Film deleteLikeFromFilm(long filmId, long userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        if(film == null) {
            throw new ObjectNotFoundException("Фильм", filmId);
        }
        if(user == null) {
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        checkFilmLikesList(film);
        film.getLikes().remove(userId);
        return filmStorage.update(film);
    }

    public List<Film> getMostLikedFilms(int quantity) {
        List<Film> popularFilms = filmStorage.getAll();
        for(Film film : popularFilms) checkFilmLikesList(film);
        return popularFilms.stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(Math.max(quantity, 0)).collect(Collectors.toList());
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}
