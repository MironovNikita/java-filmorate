package ru.yandex.practicum.filmorate.film.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.common.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.common.mapper.Mapper;
import ru.yandex.practicum.filmorate.film.dao.FilmDao;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.film.storage.FilmStorage;
import ru.yandex.practicum.filmorate.genre.storage.GenreStorage;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.mpa.storage.MpaStorage;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;

import java.util.List;


@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final Mapper<FilmDao, Film> filmDaoToFilmTransformer;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       GenreStorage genreStorage,
                       MpaStorage mpaStorage,
                       Mapper<FilmDao, Film> filmDaoToFilmTransformer) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.filmDaoToFilmTransformer = filmDaoToFilmTransformer;
    }

    public Film create(FilmDao filmDao) {
        Film checkFilm = filmDaoToFilmTransformer.transformFrom(filmDao);
        Mpa checkMpa = mpaStorage.getById(checkFilm.getMpa().getId());

        if (checkMpa == null) {
            log.error("Ошибка! Рейтинга с таким id '{}' не существует!", checkFilm.getMpa().getId());
            throw new ObjectNotFoundException("Рейтинг", checkFilm.getMpa().getId());
        }

        checkFilm.getGenres().forEach(genre -> {
            if (genreStorage.getById(genre.getId()) == null) {
                log.error("Ошибка! Жанра с таким id '{}' не существует!", genre.getId());
                throw new ObjectNotFoundException("Жанр", genre.getId());
            }
        });
        log.info("Команда сервиса на создание фильма прошла успешно");
        return filmStorage.create(checkFilm);
    }

    public Film update(FilmDao filmDao) {
        Film filmExistCheck = filmStorage.getById(filmDao.getId());
        if (filmExistCheck == null) {
            log.error("Ошибка! Фильма с id " + filmDao.getId() + " не существует!");
            throw new ObjectNotFoundException("Фильм", filmDao.getId());
        }
        Film filmToUpdate = filmDaoToFilmTransformer.transformFrom(filmDao);

        if (mpaStorage.getById(filmToUpdate.getMpa().getId()) == null) {
            log.error("Ошибка! Рейтинга с id " + filmToUpdate.getMpa().getId() + " не существует!");
            throw new ObjectNotFoundException("Рейтинг", filmToUpdate.getMpa().getId());
        }

        filmToUpdate.getGenres().forEach(genre -> {
            if (genreStorage.getById(genre.getId()) == null) {
                log.error("Ошибка! Жанра с таким id '{}' не существует!", genre.getId());
                throw new ObjectNotFoundException("Жанр", genre.getId());
            }
        });
        log.info("Команда сервиса на обновление фильма прошла успешно");
        return filmStorage.update(filmToUpdate.getId(), filmToUpdate);
    }

    public Film getById(long id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            log.error("Фильм с id '{}' не найден", id);
            throw new ObjectNotFoundException("Фильм", id);
        }
        return film;
    }

    public List<Film> getAll() {
        log.info("Команда сервиса на запрос списка всех фильмов выполнена успешно");
        return filmStorage.getAll();
    }

    public Film delete(long id) {
        Film film = filmStorage.delete(id);
        if (film == null) {
            log.error("Фильм с id '{}' не найден", id);
            throw new ObjectNotFoundException("Фильм", id);
        }
        log.info("Команда сервиса на удаление фильма с id '{}' была успешно выполнена", id);
        return film;
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
        filmStorage.likeFilm(filmId, userId);
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
        filmStorage.deleteLikeFromFilm(filmId, userId);
    }

    public List<Film> getMostLikedFilms(int quantity) {
        log.info("Команда сервиса на запрос самых популярных фильмов успешно выполнена");
        return filmStorage.getMostLikedFilms(quantity);
    }
}
