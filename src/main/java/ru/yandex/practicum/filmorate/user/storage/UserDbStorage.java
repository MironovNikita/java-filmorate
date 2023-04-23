package ru.yandex.practicum.filmorate.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.user.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userDbStorage")
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Map<String, Object> userTable = new HashMap<>();
        userTable.put("email", user.getEmail());
        userTable.put("login", user.getLogin());
        userTable.put("name", user.getName());
        userTable.put("birthday", user.getBirthday());

        long id = simpleJdbcInsert.executeAndReturnKey(userTable).longValue();
        log.info("Запрос на создание пользователя с логином '{}'", user.getLogin());
        return getById(id);
    }

    @Override
    public User update(long id, User user) {
        jdbcTemplate.update(UserRequests.UPDATE, user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday(), id);
        log.info("Запрос на обновление пользователя с id '{}'", id);
        return getById(id);
    }

    @Override
    public User getById(long id) {
        try {
            log.info("Запрошен поиск пользователя по id '{}'", id);
            return jdbcTemplate.queryForObject(UserRequests.GET_BY_ID, this::userMapping, id);
        } catch (DataAccessException exception) {
            log.warn("Пользователь с id '{}' не найден", id);
            return null;
        }
    }

    @Override
    public User getByEmail(String email) {
        try {
            log.info("Запрошен поиск пользователя по email '{}'", email);
            return jdbcTemplate.queryForObject(UserRequests.GET_BY_EMAIL, this::userMapping, email);
        } catch (DataAccessException exception) {
            log.warn("Пользователь с email '{}' не найден", email);
            return null;
        }
    }

    @Override
    public List<User> getAll() {
        log.info("Запрошен список всех пользователей");
        return jdbcTemplate.query(UserRequests.GET_ALL, this::userMapping);
    }

    @Override
    public User delete(long id) {
        User userToDelete = getById(id);
        if(userToDelete == null) {
            log.info("Запрос на удаление пользователя не выполнен. Пользователь с id '{}' отсутствует", id);
            return null;
        }
        jdbcTemplate.update(UserRequests.DELETE, id);
        log.info("Запрос на удаление пользователя с id '{}' выполнен", id);
        return userToDelete;
    }

    @Override
    public List<User> getFriends(long id) {
        log.info("Запрошен список друзей пользователя с id '{}'", id);
        return jdbcTemplate.query(UserRequests.GET_FRIENDS, this::userMapping, id);
    }

    @Override
    public List<User> getCommonFriends(long userId, long anotherUserId) {
        log.info("Запрошен список общих друзей пользователей с id '{}' и с id '{}'", userId, anotherUserId);
        return jdbcTemplate.query(UserRequests.GET_COMMON_FRIENDS, this::userMapping, userId, anotherUserId);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        log.info("Запрос на добавление в друзья пользователей с id '{}' и с id '{}'", userId, friendId);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("friendship");
        Map<String, Object> userTable = new HashMap<>();
        userTable.put("user_id", userId);
        userTable.put("friend_id", friendId);

        simpleJdbcInsert.execute(userTable);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        log.info("Запрос на удаление из друзей пользователей с id '{}' и с id '{}'", userId, friendId);
        jdbcTemplate.update(UserRequests.DELETE_FRIEND, userId, friendId);
    }

    private User userMapping(ResultSet resultSet, int rowNumber) throws SQLException {
        return new User(resultSet.getLong("user_id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate());
    }
}
