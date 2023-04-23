package ru.yandex.practicum.filmorate.film.storage;

import ru.yandex.practicum.filmorate.film.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film update(long id, Film film);

    Film getById(long id);

    List<Film> getAll();

    Film delete(long id);

    List<Film> getMostLikedFilms(int quantity);

    void likeFilm(long filmId, long userId);

    void deleteLikeFromFilm(long filmId, long userId);
}
