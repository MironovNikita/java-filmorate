package ru.yandex.practicum.filmorate.storageInMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EmptyObjectException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long uniqId;

    public long generateId() {
        return ++uniqId;
    }

    @Override
    public List<User> getAll() {
        log.info("Запрошен список всех пользователей.");
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(long id) {
        if(users.get(id) != null) {
            log.info("Запрошен пользователь с id '{}'", id);
            return users.get(id);
        } else {
            log.warn("Пользователь не найден. Передан отсутствующий id пользователя.");
            throw new ObjectNotFoundException("Пользователь", id);
        }
    }

    @Override
    public User create(User user) {
        if(user == null) {
            log.warn("Невозможно создать объект пользователя. Был передан пустой объект");
            throw new EmptyObjectException("Невозможно создать объект пользователя. Был передан пустой объект");
        }
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("У пользователя было пустое имя. В качестве имени задан логин '{}'", user.getLogin());
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Пользователь '{}' с id '{}' был успешно добавлен.", user.getName(), user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        if(user == null) {
            log.warn("Невозможно обновить объект пользователя. Был передан пустой объект");
            throw new EmptyObjectException("Невозможно обновить объект пользователя. Был передан пустой объект");
        }
        if(users.get(user.getId()) == null) {
            log.warn("Запрос на обновление пользователя с id '{}' отклонён. Он отсутствует в списке пользователей.",
                    user.getId());
            throw new RuntimeException("Список пользователей не содержит такого ID");
        }
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("У пользователя было пустое имя. В качестве имени задан логин '{}'", user.getLogin());
        }
        if(users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь '{}' с id '{}' был успешно обновлён.", user.getName(), user.getId());
        }
        return user;
    }

    @Override
    public User delete(long id) {
        if(users.get(id) != null) {
            log.info("Запрос на удаление пользователя с id '{}'", id);
            return users.remove(id);
        } else {
            log.warn("Пользователь не найден. Передан отсутствующий id пользователя");
            throw new ObjectNotFoundException("Пользователь", id);
        }
    }
}
