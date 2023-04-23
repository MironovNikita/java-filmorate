package ru.yandex.practicum.filmorate.user.storage;

import ru.yandex.practicum.filmorate.user.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(long id, User user);

    User getById(long id);

    User getByEmail(String email);

    List<User> getAll();

    User delete(long id);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> getFriends(long userId);

    List<User> getCommonFriends(long userId, long anotherUserId);
}
