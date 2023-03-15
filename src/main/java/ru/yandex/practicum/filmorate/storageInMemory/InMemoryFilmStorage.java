package ru.yandex.practicum.filmorate.storageInMemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long uniqId;

    public long generateId() {
        return ++uniqId;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(long id) {
        return (films.get(id));
    }

    @Override
    public Film create(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if(films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film delete(long id) {
        return films.remove(id);
    }
}
