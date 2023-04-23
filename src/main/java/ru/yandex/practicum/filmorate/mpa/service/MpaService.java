package ru.yandex.practicum.filmorate.mpa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.common.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.mpa.storage.MpaStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa getById(int id) {
        Mpa mpa = mpaStorage.getById(id);
        if (mpa == null) {
            log.warn("Команда сервиса жанров фильмов не выполнена. Жанр с id '{}' не найден", id);
            throw new ObjectNotFoundException("Жанр", id);
        }
        log.info("Команда сервиса жанров фильмов выполнена. Запрошен жанр с id '{}'", id);
        return mpa;
    }

    public List<Mpa> getAll() {
        log.info("Команда сервиса жанров фильмов выполнена. Запрошены все жанры фильмов");
        return mpaStorage.getAll();
    }
}
