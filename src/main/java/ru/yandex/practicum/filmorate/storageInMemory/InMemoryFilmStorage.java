package ru.yandex.practicum.filmorate.storageInMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DateValidationException;
import ru.yandex.practicum.filmorate.exception.EmptyObjectException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long uniqId;

    public long generateId() {
        return ++uniqId;
    }

    @Override
    public List<Film> getAll() {
        log.info("Запрошен список всех фильмов.");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(long id) {
        if(films.get(id) != null) {
            log.info("Запрошен фильм с id '{}' ", id);
            return films.get(id);
        } else {
            log.warn("Фильм не найден. Передан отсутствующий id фильма");
            throw new ObjectNotFoundException("Фильм", id);
        }
    }

    @Override
    public Film create(Film film) {
        if(film == null) {
            log.warn("Невозможно создать объект фильма. Был передан пустой объект");
            throw new EmptyObjectException("Невозможно создать объект фильма. Был передан пустой объект");
        }
        if(film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895,12,
                28))) {
            log.warn("Фильм не может быть выпущен раньше дня рождения кино!");
            throw new DateValidationException("Фильм не может быть выпущен раньше дня рождения кино!");
        }
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Фильм '{}' с id '{}' был успешно добавлен.", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        if(film == null) {
            log.warn("Невозможно обновить объект фильма. Был передан пустой объект");
            throw new EmptyObjectException("Невозможно обновить объект фильма. Был передан пустой объект");
        }
        if(films.get(film.getId()) == null) {
            log.warn("Запрос на обновление фильма с id '{}' отклонён. Он отсутствует в списке фильмов.", film.getId());
            throw new ObjectNotFoundException(film.getName(), film.getId());
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.warn("Фильм не может быть выпущен раньше дня рождения кино!");
            throw new DateValidationException("Фильм не может быть выпущен раньше дня рождения кино!");
        }
        if(films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм '{}' с id '{}' был успешно обновлён.", film.getName(), film.getId());
        }
        return film;
    }

    @Override
    public Film delete(long id) {
        if(films.get(id) != null) {
            log.info("Запрошено удаление фильма с id '{}'", id);
            return films.remove(id);
        } else {
            log.warn("Фильм не найден. Передан отсутствующий id фильма");
            throw new ObjectNotFoundException("Фильм", id);
        }
    }
}
