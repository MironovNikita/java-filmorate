package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
@Slf4j
@Component
public class ValidateFilmService {
    public Film validateFilm(Film film) {
        if(film.getName().isBlank()) {
            log.warn("Запрос на добавление фильма '{}' с id '{}' отклонён. У фильма пустое название.", film.getName(),
                    film.getId());
            throw new ValidationException("Название фильма не может быть пустым!");
        }
        if(film.getDescription().length() > 200) {
            log.warn("Запрос на добавление фильма с id '{}' отклонён. У фильма слишком длинное описание.",
                    film.getId());
            throw new ValidationException("Превышен лимит символов описания фильма!");
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.warn("Запрос на добавление фильма с id '{}' отклонён. У фильма слишком ранняя дата релиза: '{}'.",
                    film.getId(), film.getReleaseDate());
            throw new ValidationException("У фильма слишком ранняя дата релиза!");
        }
        if (film.getDuration() <= 0) {
            log.warn("Запрос на добавление фильма с id '{}' отклонён. У фильма некорректная продолжительность: '{}'.",
                    film.getId(), film.getDuration());
            throw new ValidationException("У фильма некорректная продолжительность!");
        }
        return film;
    }
}
