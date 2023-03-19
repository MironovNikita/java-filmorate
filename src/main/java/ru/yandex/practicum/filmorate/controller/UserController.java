package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectPathDataException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid User user) {
        userService.getUserStorage().create(user);
        return user;
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody @Valid User user) { //не может его содержать, потому что его там нет!!!
        userService.getUserStorage().update(user);
        return user;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> showAllUsersList() {
        return new ArrayList<>(userService.getUserStorage().getAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Optional<Long> id) {
        if(id.isPresent()) {
            long correctId = id.get();
            return userService.getUserStorage().getById(correctId);
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
            return userService.getUserStorage().delete(correctId);
        } else {
            log.warn("Пользователь не удалён. Передан некорректный id пользователя.");
            throw new IncorrectPathDataException("Пользователь не удалён. Передан некорректный id пользователя.");
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User addFriend(@PathVariable("id") Optional<Long> userId, @PathVariable Optional<Long> friendId) {
        if(userId.isPresent() && friendId.isPresent()) {
            long correctFriendId = friendId.get();
            long correctUserId = userId.get();
            return userService.addFriend(correctUserId, correctFriendId);
        } else {
            log.warn("Проверьте запрос. Некорректный id пользователя.");
            throw new IncorrectPathDataException("Проверьте запрос. Некорректный id пользователя.");
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User deleteFriend(@PathVariable("id") Optional<Long> userId, @PathVariable Optional<Long> friendId) {
        if (userId.isPresent() && friendId.isPresent()) {
            long correctFriendId = friendId.get();
            long correctUserId = userId.get();
            return userService.deleteFriend(correctUserId, correctFriendId);
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
            return userService.getCommonFriends(correctUserId, correctOtherId);
        } else {
            log.warn("Проверьте запрос. Некорректный id пользователя.");
            throw new IncorrectPathDataException("Проверьте запрос. Некорректный id пользователя.");
        }
    }
}