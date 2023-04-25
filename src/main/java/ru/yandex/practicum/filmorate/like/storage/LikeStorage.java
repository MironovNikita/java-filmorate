package ru.yandex.practicum.filmorate.like.storage;

import ru.yandex.practicum.filmorate.film.model.Film;

import java.util.List;

public interface LikeStorage {
    List<Film> getMostLikedFilms(int quantity);

    void likeFilm(long filmId, long userId);

    void deleteLikeFromFilm(long filmId, long userId);
}
