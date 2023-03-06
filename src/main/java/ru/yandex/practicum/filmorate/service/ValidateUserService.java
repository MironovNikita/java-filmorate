package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
@Component
public class ValidateService {
    public User validateUser(User user) {
        if(user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Запрос на создание пользователя с e-mail '{}' отклонён. Некорректный e-mail.",
                    user.getEmail());
            throw new ValidationException("У пользователя некорректный e-mail!");
        }
        if(user.getLogin().isBlank()) {
            log.warn("Запрос на создание пользователя с логином '{}' отклонён. Некорректный логин.",
                    user.getLogin());
            throw new ValidationException("У пользователя некорректный логин!");
        }
        if(user.getName() == null || user.getName().isBlank()) {
            log.info("Имя пользователя было пустым. В качестве имени задан логин '{}'.",
                    user.getLogin());
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Запрос на добавление пользователя с днём рождения '{}' отклонён. У пользователя некорректная " +
                    "дата рождения.", user.getBirthday());
            throw new ValidationException("У пользователя некорректная дата рождения!");
        }
        return user;
    }

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
