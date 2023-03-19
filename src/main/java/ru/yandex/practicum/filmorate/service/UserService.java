package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User addFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if(user == null) {
            log.warn("Пользователь не найден. Передан отсутствующий id пользователя.");
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        if(friend == null) {
            log.warn("Пользователь не найден. Передан отсутствующий id пользователя.");
            throw new ObjectNotFoundException("Пользователь", friendId);
        }
        checkUserFriendList(user);
        checkUserFriendList(friend);
        userStorage.getById(userId).getFriends().add(friendId);
        userStorage.getById(friendId).getFriends().add(userId);
        userStorage.getById(userId).setFriendsNumber(userStorage.getById(userId).getFriendsNumber() + 1);
        userStorage.getById(friendId).setFriendsNumber(userStorage.getById(friendId).getFriendsNumber() + 1);
        log.info("Пользователь с id '{}' взаимодобавил друга с id '{}' :)", userId, friendId);
        return userStorage.getById(userId);
    }

    public User deleteFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if(user == null) {
            log.warn("Пользователь не найден. Передан отсутствующий id пользователя.");
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        if(friend == null) {
            log.warn("Пользователь не найден. Передан отсутствующий id пользователя.");
            throw new ObjectNotFoundException("Пользователь", friendId);
        }
        userStorage.getById(userId).getFriends().remove(friendId);
        userStorage.getById(friendId).getFriends().remove(userId);
        userStorage.getById(userId).setFriendsNumber(userStorage.getById(userId).getFriendsNumber() - 1);
        userStorage.getById(friendId).setFriendsNumber(userStorage.getById(friendId).getFriendsNumber() - 1);
        log.info("Пользователь с id '{}' взаимоудалил друга с id '{}' :(", userId, friendId);
        return userStorage.getById(userId);
    }

    public void checkUserFriendList(User user) {
        if(user.getFriends() == null) {
            user.setFriends(new HashSet<Long>());
        }
    }

    public List<User> getFriends(long userId) {
        User user = userStorage.getById(userId);
        if(user == null) {
            log.warn("Пользователь не найден. Передан отсутствующий id пользователя");
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        checkUserFriendList(user);
        log.info("Запрошен список друзей пользователя с id '{}'", userId);
        return user.getFriends().stream().map(userStorage::getById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long someUserId) {
        User user = userStorage.getById(userId);
        User someUser = userStorage.getById(someUserId);
        if(user == null) {
            log.warn("Пользователь не найден. Передан отсутствующий id пользователя.");
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        if(someUser == null) {
            log.warn("Пользователь не найден. Передан отсутствующий id пользователя.");
            throw new ObjectNotFoundException("Пользователь", someUserId);
        }
        checkUserFriendList(user);
        checkUserFriendList(someUser);
        log.info("Запрошен список общих друзей пользователей с id '{}' и id '{}'", userId,
                someUserId);
        return user.getFriends().stream().filter(userFriendId -> someUser.getFriends()
                        .contains(userFriendId)).map(userStorage::getById).collect(Collectors.toList());
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}
