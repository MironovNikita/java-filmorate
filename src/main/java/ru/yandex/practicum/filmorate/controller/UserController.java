package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectPathDataException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
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
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("У пользователя было пустое имя. В качестве имени задан логин '{}'", user.getLogin());
        }
        userService.getUserStorage().create(user);
        log.info("Пользователь '{}' с id '{}' был успешно добавлен.", user.getName(), user.getId());
        return user;
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody @Valid User user) { //не может его содержать, потому что его там нет!!!
        if(userService.getUserStorage().getById(user.getId()) == null) {
            log.warn("Запрос на обновление пользователя с id '{}' отклонён. Он отсутствует в списке пользователей.",
                    user.getId());
            throw new RuntimeException("Список пользователей не содержит такого ID");
        }
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("У пользователя было пустое имя. В качестве имени задан логин '{}'", user.getLogin());
        }
        userService.getUserStorage().update(user);
        log.info("Пользователь '{}' с id '{}' был успешно обновлён.", user.getName(), user.getId());
        return user;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> showAllUsersList() {
        log.info("Запрошен список всех пользователей.");
        return new ArrayList<>(userService.getUserStorage().getAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Optional<Long> id) {
        if(id.isPresent()) {
            long correctId = id.get();
            if(userService.getUserStorage().getById(correctId) != null) {
                log.info("Запрошен пользователь с id '{}'", correctId);
                return userService.getUserStorage().getById(correctId);
            } else {
                log.warn("Пользователь не найден. Передан отсутствующий id пользователя.");
                throw new ObjectNotFoundException("Пользователь", correctId);
            }
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
            if(userService.getUserStorage().getById(correctId) != null) {
                log.info("Запрос на удаление пользователя с id '{}'", correctId);
                return userService.getUserStorage().delete(correctId);
            } else {
                log.warn("Пользователь не найден. Передан отсутствующий id пользователя");
                throw new ObjectNotFoundException("Пользователь", correctId);
            }
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
            User user = userService.getUserStorage().getById(correctUserId);
            User friend = userService.getUserStorage().getById(correctFriendId);
            if (user != null && friend != null) {
                log.info("Пользователь с id '{}' взаимодобавил друга с id '{}' :)", correctUserId, correctFriendId);
                return userService.addFriend(correctUserId, correctFriendId);
            } else {
                log.warn("Пользователь не найден. Передан отсутствующий id пользователя.");
                if (user == null) {
                    throw new ObjectNotFoundException("Пользователь", correctUserId);
                } else {
                    throw new ObjectNotFoundException("Пользователь", correctFriendId);
                }
            }
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
            User user = userService.getUserStorage().getById(correctUserId);
            User friend = userService.getUserStorage().getById(correctFriendId);
            if (user != null && friend != null) {
                log.info("Пользователь с id '{}' взаимоудалил друга с id '{}' :(", correctUserId, correctFriendId);
                return userService.deleteFriend(correctUserId, correctFriendId);
            } else {
                log.warn("Пользователь не найден. Передан отсутствующий id пользователя.");
                if (user == null) {
                    throw new ObjectNotFoundException("Пользователь", correctUserId);
                } else {
                    throw new ObjectNotFoundException("Пользователь", correctFriendId);
                }
            }
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
            if(userService.getUserStorage().getById(correctId) != null) {
                log.info("Запрошен список друзей пользователя с id '{}'", correctId);
                return userService.getFriends(correctId);
            } else {
                log.warn("Пользователь не найден. Передан отсутствующий id пользователя");
                throw new ObjectNotFoundException("Пользователь", correctId);
            }
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
            User user = userService.getUserStorage().getById(correctUserId);
            User otherUser = userService.getUserStorage().getById(correctOtherId);
            if(user != null && otherUser != null) {
                log.info("Запрошен список общих друзей пользователей с id '{}' и id '{}'", correctUserId,
                        correctOtherId);
                return userService.getCommonFriends(correctUserId, correctOtherId);
            } else {
                log.warn("Пользователь не найден. Передан отсутствующий id пользователя.");
                if (user == null) {
                    throw new ObjectNotFoundException("Пользователь", correctUserId);
                } else {
                    throw new ObjectNotFoundException("Пользователь", correctOtherId);
                }
            }
        } else {
            log.warn("Проверьте запрос. Некорректный id пользователя.");
            throw new IncorrectPathDataException("Проверьте запрос. Некорректный id пользователя.");
        }
    }
}