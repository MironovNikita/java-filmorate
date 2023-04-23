package ru.yandex.practicum.filmorate.user.storage;

public class UserRequests {
    static final String UPDATE = "UPDATE users " +
            "SET " +
            "email = ?, " +
            "login = ?, " +
            "name = ?, " +
            "birthday = ? " +
            "WHERE user_id = ?";

    static final String GET_BY_ID = "SELECT * " +
            "FROM users " +
            "WHERE user_id = ?";

    static final String GET_BY_EMAIL = "SELECT * " +
            "FROM users " +
            "WHERE email = ?";

    static final String GET_ALL = "SELECT * " +
            "FROM users";

    static final String DELETE = "DELETE " +
            "FROM users " +
            "WHERE user_id = ?";

    static final String DELETE_FRIEND = "DELETE " +
            "FROM friendship " +
            "WHERE user_id = ? AND friend_id = ?";

    static final String GET_FRIENDS = "SELECT " +
            "u.user_id, " +
            "u.email, " +
            "u.login, " +
            "u.name, " +
            "u.birthday " +
            "FROM friendship AS f " +
            "INNER JOIN users AS u ON u.user_id = f.friend_id " +
            "WHERE f.user_id = ?";

    static final String GET_COMMON_FRIENDS = "SELECT " +
            "u.user_id, " +
            "u.email, " +
            "u.login, " +
            "u.name, " +
            "u.birthday " +
            "FROM friendship AS f " +
            "INNER JOIN users AS u ON u.user_id = f.friend_id " +
            "WHERE f.user_id = ? AND f.friend_id IN (" +
            "SELECT friend_id " +
            "FROM friendship " +
            "WHERE user_id = ?)";
}
