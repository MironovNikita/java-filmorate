package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User addFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if(user == null) {
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        if(friend == null) {
            throw new ObjectNotFoundException("Пользователь", friendId);
        }
        checkUserFriendList(user);
        checkUserFriendList(friend);
        userStorage.getById(userId).getFriends().add(friendId);
        userStorage.getById(friendId).getFriends().add(userId);
        return userStorage.getById(userId);
    }

    public User deleteFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if(user == null) {
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        if(friend == null) {
            throw new ObjectNotFoundException("Пользователь", friendId);
        }
        userStorage.getById(userId).getFriends().remove(friendId);
        userStorage.getById(friendId).getFriends().remove(userId);
        return userStorage.getById(userId);
    }

    private void checkUserFriendList(User user) {
        if(user.getFriends() == null) {
            user.setFriends(new HashSet<Long>());
        }
    }

    public List<User> getFriends(long userId) {
        User user = userStorage.getById(userId);
        if(user == null) {
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        checkUserFriendList(user);
        return user.getFriends().stream().map(userStorage::getById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long someUserId) {
        User user = userStorage.getById(userId);
        User someUser = userStorage.getById(someUserId);
        if(user == null) {
            throw new ObjectNotFoundException("Пользователь", userId);
        }
        if(someUser == null) {
            throw new ObjectNotFoundException("Пользователь", someUserId);
        }
        checkUserFriendList(user);
        checkUserFriendList(someUser);
        return user.getFriends().stream().filter(userFriendId -> someUser.getFriends()
                        .contains(userFriendId)).map(userStorage::getById).collect(Collectors.toList());
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}
