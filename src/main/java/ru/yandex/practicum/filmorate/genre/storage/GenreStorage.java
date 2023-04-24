package ru.yandex.practicum.filmorate.genre.storage;

import ru.yandex.practicum.filmorate.genre.model.Genre;

import java.util.LinkedHashSet;
import java.util.List;

public interface GenreStorage {
    Genre getById(int id);

    List<Genre> getAll();

    LinkedHashSet<Genre> getAllGenresByFilmId(long filmId);
}
