package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
@Component
public class ValidateUserService {
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
}
