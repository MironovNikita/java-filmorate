package ru.yandex.practicum.filmorate.like.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.common.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.film.storage.FilmStorage;
import ru.yandex.practicum.filmorate.like.storage.LikeStorage;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class LikeService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    public LikeService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("likeDbStorage") LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public void likeFilm(long filmId, long userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        if (film == null) {
            log.error("Фильм с id '{}' не найден", filmId);
            throw new ObjectNotFoundException("Фильм", filmId);
        }
        if (user == null) {
            log.error("Пользователь с id '{}' не найден", userId);
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        log.info("Команда сервиса на постановку лайка фильму с id '{}' от пользователя с id '{}' была успешно" +
                "выполнена", filmId, userId);
        likeStorage.likeFilm(filmId, userId);
    }

    public void deleteLikeFromFilm(long filmId, long userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        if (film == null) {
            log.error("Фильм с id '{}' не найден", filmId);
            throw new ObjectNotFoundException("Фильм", filmId);
        }
        if (user == null) {
            log.error("Пользователь с id '{}' не найден", userId);
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        log.info("Команда сервиса на постановку лайка фильму с id '{}' от пользователя с id '{}' была успешно" +
                "выполнена", filmId, userId);
        likeStorage.deleteLikeFromFilm(filmId, userId);
    }

    public List<Film> getMostLikedFilms(int quantity) {
        log.info("Команда сервиса на запрос самых популярных фильмов успешно выполнена");
        return likeStorage.getMostLikedFilms(quantity);
    }
}
