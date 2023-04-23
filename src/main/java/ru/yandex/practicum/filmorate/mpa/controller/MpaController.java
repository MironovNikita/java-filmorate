package ru.yandex.practicum.filmorate.mpa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.common.exception.IncorrectPathDataException;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.mpa.service.MpaService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable ("id") Optional<Integer> mpaId) {
        if(mpaId.isPresent()) {
            int correctId = mpaId.get();
            log.info("Выполнение команды контроллера на получение рейтинга фильма по id '{}'", correctId);
            return mpaService.getById(correctId);
        } else {
            log.warn("Рейтинг фильма не найден. Передан некорректный id рейтинга.");
            throw new IncorrectPathDataException("Рейтинг фильма не найден. Передан некорректный id рейтинга..");
        }

    }

    @GetMapping
    public List<Mpa> getAllMpa() {
        log.info("Команда контроллера на получение списка всех жанров");
        return mpaService.getAll();
    }
}
