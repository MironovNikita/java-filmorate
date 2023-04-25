package ru.yandex.practicum.filmorate.friend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.common.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.friend.storage.FriendStorage;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FriendService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public FriendService(@Qualifier("userDbStorage") UserStorage userStorage,
                         @Qualifier("friendDbStorage") FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public void addFriend(long userId, long friendId) {
        if (userId == friendId) {
            log.error("Ваш id '{}', id друга - '{}'. Добавление самого себя в друзья невозможно", userId, friendId);
            throw new RuntimeException("Пользователь не может добавить в друзья сам себя");
        }

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if (user == null) {
            log.error("Пользователь с id '{}' не существует", userId);
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        if (friend == null) {
            log.error("Пользователь с id '{}' не существует", friendId);
            throw new ObjectNotFoundException("Пользователь", friendId);
        }
        log.info("Команда сервиса на добавление в друзья пользователей с id '{}' и '{}' выполнена успешно", userId,
                friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        if (userId == friendId) {
            log.error("Ваш id '{}', id друга - '{}'. Удаление самого себя из друзей невозможно", userId, friendId);
            throw new RuntimeException("Пользователь не может удалить из друзей самого себя. Придётся терпеть :)");
        }
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if (user == null) {
            log.error("Пользователь с id '{}' не существует", userId);
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        if (friend == null) {
            log.error("Пользователь с id '{}' не существует", friendId);
            throw new ObjectNotFoundException("Пользователь", friendId);
        }
        log.info("Команда сервиса на удаление из друзей пользователей с id '{}' и '{}' выполнена успешно", userId,
                friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            log.error("Пользователь с id '{}' не существует", id);
            throw new ObjectNotFoundException("Пользователь", id);
        }
        log.info("Команда сервиса на запрос списка друзей пользователя с id '{}' выполнена успешно", id);
        return friendStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long userId, long anotherUserId) {
        User user = userStorage.getById(userId);
        User anotherUser = userStorage.getById(anotherUserId);
        if (user == null) {
            log.error("Пользователь с id '{}' не существует", userId);
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        if (anotherUser == null) {
            log.error("Пользователь с id '{}' не существует", anotherUserId);
            throw new ObjectNotFoundException("Пользователь", anotherUserId);
        }
        log.info("Команда сервиса по получению списка общих друзей пользователей с id '{}' и '{}' выполнена успешно",
                userId, anotherUserId);
        return friendStorage.getCommonFriends(userId, anotherUserId);
    }
}
