package ru.yandex.practicum.filmorate.genre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.common.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.genre.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getById(int id) {
        Genre genre = genreStorage.getById(id);
        if(genre == null) {
            log.warn("Команда сервиса на запрос жанра по id '{}' не выполнена. Жанр не найден", id);
            throw new ObjectNotFoundException("Жанр", id);
        }
        log.info("Команда сервиса на запрос жанра по id '{}' выполнена", id);
        return genre;
    }

    public List<Genre> getAll() {
        log.info("Команда сервиса на запрос списка всех жанров");
        return genreStorage.getAll();
    }
}
