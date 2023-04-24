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
}
