package ru.yandex.practicum.filmorate.data;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long uniqId;

    public Map<Long, User> getUsers() {
        return users;
    }

    public long generateId() {
        return ++uniqId;
    }

    public void saveUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
    }
}
