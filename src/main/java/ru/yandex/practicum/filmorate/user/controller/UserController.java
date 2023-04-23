package ru.yandex.practicum.filmorate.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.common.exception.IncorrectPathDataException;
import ru.yandex.practicum.filmorate.user.dao.UserDao;
import ru.yandex.practicum.filmorate.user.service.UserService;
import ru.yandex.practicum.filmorate.user.model.User;

import javax.validation.Valid;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid UserDao userDao) {
        log.info("Выполнение команды контроллера на создание пользователя " + userDao);
        return userService.create(userDao);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody @Valid UserDao userDao) {
        log.info("Выполнение команды контроллера на обновление пользователя " + userDao);
        return userService.update(userDao);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> showAllUsersList() {
        log.info("Выполнение команды контроллера на получение списка всех пользователей");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Optional<Long> id) {
        if(id.isPresent()) {
            long correctId = id.get();
            log.info("Выполнение команды контроллера на получение пользователя по id '{}'", correctId);
            return userService.getById(correctId);
        } else {
            log.warn("Пользователь не найден. Передан некорректный id пользователя.");
            throw new IncorrectPathDataException("Пользователь не найден. Некорректный id пользователя.");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User deleteUser(@PathVariable Optional<Long> id) {
        if(id.isPresent()) {
            long correctId = id.get();
            log.info("Выполнение команды контроллера на удаление пользователя по id '{}'", correctId);
            return userService.delete(correctId);
        } else {
            log.warn("Пользователь не удалён. Передан некорректный id пользователя.");
            throw new IncorrectPathDataException("Пользователь не удалён. Передан некорректный id пользователя.");
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable("id") Optional<Long> userId, @PathVariable Optional<Long> friendId) {
        if(userId.isPresent() && friendId.isPresent()) {
            long correctFriendId = friendId.get();
            long correctUserId = userId.get();
            log.info("Выполнение команды контроллера на добавление в друзья пользователей с id '{}' и '{}'",
                    correctUserId, correctFriendId);
            userService.addFriend(correctUserId, correctFriendId);
        } else {
            log.warn("Проверьте запрос. Некорректный id пользователя.");
            throw new IncorrectPathDataException("Проверьте запрос. Некорректный id пользователя.");
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable("id") Optional<Long> userId, @PathVariable Optional<Long> friendId) {
        if (userId.isPresent() && friendId.isPresent()) {
            long correctFriendId = friendId.get();
            long correctUserId = userId.get();
            log.info("Выполнение команды контроллера на удаление из друзей пользователей с id '{}' и '{}'",
                    correctUserId, correctFriendId);
            userService.deleteFriend(correctUserId, correctFriendId);
        } else {
            log.warn("Проверьте запрос. Некорректный id пользователя.");
            throw new IncorrectPathDataException("Проверьте запрос. Некорректный id пользователя.");
        }
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriendsOfUser(@PathVariable("id") Optional<Long> userId) {
        if(userId.isPresent()) {
            long correctId = userId.get();
            log.info("Выполнение команды контроллера на получение списка друзей пользователя с id '{}'", correctId);
            return userService.getFriends(correctId);
        } else {
            log.warn("Список друзей не обнаружен. Некорректный id пользователя.");
            throw new IncorrectPathDataException("Список друзей не обнаружен. Некорректный id пользователя.");
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriendsOfTwoUsers(@PathVariable("id") Optional<Long> userId,
                                                 @PathVariable("otherId") Optional<Long> otherUserId) {
        if(userId.isPresent() && otherUserId.isPresent()) {
            long correctOtherId = otherUserId.get();
            long correctUserId = userId.get();
            log.info("Выполнение команды контроллера на получение общих друзей пользователей с id '{}' и '{}'",
                    correctUserId, correctOtherId);
            return userService.getCommonFriends(correctUserId, correctOtherId);
        } else {
            log.warn("Проверьте запрос. Некорректный id пользователя.");
            throw new IncorrectPathDataException("Проверьте запрос. Некорректный id пользователя.");
        }
    }
}