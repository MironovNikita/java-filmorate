package ru.yandex.practicum.filmorate.friend.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.user.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("friendDbStorage")
@RequiredArgsConstructor
@Slf4j
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getFriends(long id) {
        log.info("Запрошен список друзей пользователя с id '{}'", id);
        return jdbcTemplate.query(FriendRequests.GET_FRIENDS, this::userMapping, id);
    }

    @Override
    public List<User> getCommonFriends(long userId, long anotherUserId) {
        log.info("Запрошен список общих друзей пользователей с id '{}' и с id '{}'", userId, anotherUserId);
        return jdbcTemplate.query(FriendRequests.GET_COMMON_FRIENDS, this::userMapping, userId, anotherUserId);
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
        jdbcTemplate.update(FriendRequests.DELETE_FRIEND, userId, friendId);
    }

    private User userMapping(ResultSet resultSet, int rowNumber) throws SQLException {
        return new User(resultSet.getLong("user_id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate());
    }
}
