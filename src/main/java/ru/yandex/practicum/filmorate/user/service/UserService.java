package ru.yandex.practicum.filmorate.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.common.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.common.mapper.Mapper;
import ru.yandex.practicum.filmorate.user.dao.UserDao;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;
import ru.yandex.practicum.filmorate.user.model.User;

import java.util.List;


@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final Mapper<UserDao, User> userDaoToUserTransformer;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       Mapper<UserDao, User> userDaoToUserTransformer) {
        this.userStorage = userStorage;
        this.userDaoToUserTransformer = userDaoToUserTransformer;
    }

    private boolean emailExists(String email) {
        return userStorage.getByEmail(email) != null;
    }

    public User create(UserDao userDao) {
        User user = userDaoToUserTransformer.transformFrom(userDao);
        if(emailExists(user.getEmail())) {
            log.error("Ошибка! Пользователь с email '{}' уже существует!", user.getEmail());
            throw new RuntimeException("Ошибка! Пользователь с таким email уже существует!");
        }
        log.info("Команда сервиса на создание пользователя прошла успешно");
        return userStorage.create(user);
    }

    public User update(UserDao userDao) {
        User userExistCheck = userStorage.getById(userDao.getId());
        if(userExistCheck == null) {
            log.error("Ошибка! Пользователя с id " + userDao.getId() + " не существует!");
            throw new ObjectNotFoundException("Пользователь", userDao.getId());
        }

        User userToUpdate = userDaoToUserTransformer.transformFrom(userDao);
        if(emailExists(userToUpdate.getEmail())) {
            log.error("Ошибка! Пользователь с email '{}' уже существует!", userToUpdate.getEmail());
            throw new RuntimeException("Ошибка! Пользователь с таким email уже существует!");
        }
        log.info("Команда сервиса на обновление пользователя прошла успешно");
        return userStorage.update(userToUpdate.getId(), userToUpdate);
    }

    public User getById(long id) {
        User user = userStorage.getById(id);
        if(user == null) {
            log.error("Пользователь с id '{}' не найден", id);
            throw new ObjectNotFoundException("Пользователь", id);
        }
        log.info("Команда сервиса по поиску пользователя с id '{}' успешно выполнена", id);
        return user;
    }

    public List<User> getAll() {
        log.info("Команда сервиса на запрос списка всех пользователей выполнена успешно");
        return userStorage.getAll();
    }

    public User delete(long id) {
        User userExistCheck = userStorage.delete(id);
        if(userExistCheck == null) {
            log.error("Пользователь с id '{}' не найден", id);
            throw new ObjectNotFoundException("Пользователь", id);
        }
        log.info("Команда сервиса на удаление пользователя с id '{}' была успешно выполнена", id);
        return userExistCheck;
    }

    public void addFriend(long userId, long friendId) {
        if(userId == friendId) {
            log.error("Ваш id '{}', id друга - '{}'. Добавление самого себя в друзья невозможно", userId, friendId);
            throw new RuntimeException("Пользователь не может добавить в друзья сам себя");
        }

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if(user == null) {
            log.error("Пользователь с id '{}' не существует", userId);
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        if(friend == null) {
            log.error("Пользователь с id '{}' не существует", friendId);
            throw new ObjectNotFoundException("Пользователь", friendId);
        }
        log.info("Команда сервиса на добавление в друзья пользователей с id '{}' и '{}' выполнена успешно", userId,
                friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        if(userId == friendId) {
            log.error("Ваш id '{}', id друга - '{}'. Удаление самого себя из друзей невозможно", userId, friendId);
            throw new RuntimeException("Пользователь не может удалить из друзей самого себя. Придётся терпеть :)");
        }
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if(user == null) {
            log.error("Пользователь с id '{}' не существует", userId);
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        if(friend == null) {
            log.error("Пользователь с id '{}' не существует", friendId);
            throw new ObjectNotFoundException("Пользователь", friendId);
        }
        log.info("Команда сервиса на удаление из друзей пользователей с id '{}' и '{}' выполнена успешно", userId,
                friendId);
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(long id) {
        User user = userStorage.getById(id);
        if(user == null) {
            log.error("Пользователь с id '{}' не существует", id);
            throw new ObjectNotFoundException("Пользователь", id);
        }
        log.info("Команда сервиса на запрос списка друзей пользователя с id '{}' выполнена успешно", id);
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long userId, long anotherUserId) {
        User user = userStorage.getById(userId);
        User anotherUser = userStorage.getById(anotherUserId);
        if(user == null) {
            log.error("Пользователь с id '{}' не существует", userId);
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        if(anotherUser == null) {
            log.error("Пользователь с id '{}' не существует", anotherUserId);
            throw new ObjectNotFoundException("Пользователь", anotherUserId);
        }
        log.info("Команда сервиса по получению списка общих друзей пользователей с id '{}' и '{}' выполнена успешно",
                userId, anotherUserId);
        return userStorage.getCommonFriends(userId, anotherUserId);
    }
}
