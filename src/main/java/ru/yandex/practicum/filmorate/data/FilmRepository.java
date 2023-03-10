package ru.yandex.practicum.filmorate.data;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

public class FilmRepository {

    private final Map<Long, Film> films = new HashMap<>();
    private long uniqId;

    public Map<Long, Film> getFilms() {
        return films;
    }

    public long generateId() {
        return ++uniqId;
    }

    public void saveFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }
}
