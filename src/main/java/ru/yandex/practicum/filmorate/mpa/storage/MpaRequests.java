package ru.yandex.practicum.filmorate.mpa.storage;

public class MpaRequests {
    static final String GET_BY_ID = "SELECT * " +
            "FROM mpa " +
            "WHERE rating_id = ?";

    static final String GET_ALL = "SELECT * " +
            "FROM mpa " +
            "ORDER BY rating_id";
}
