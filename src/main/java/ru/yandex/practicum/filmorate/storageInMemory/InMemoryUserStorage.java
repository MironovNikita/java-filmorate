package ru.yandex.practicum.filmorate.storageInMemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long uniqId;

    public long generateId() {
        return ++uniqId;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(long id) {
        return (users.get(id));
    }

    @Override
    public User create(User user) {
        user.setId(generateId());
        (users).put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if(users.containsKey(user.getId())) {
            (users).put(user.getId(), user);
        }
        return user;
    }

    @Override
    public User delete(long id) {
        return(users).remove(id);
    }
}
