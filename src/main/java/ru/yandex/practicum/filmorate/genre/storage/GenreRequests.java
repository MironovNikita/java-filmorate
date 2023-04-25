package ru.yandex.practicum.filmorate.genre.storage;

public class GenreRequests {
    static final String GET_BY_ID = "SELECT * " +
            "FROM genre " +
            "WHERE genre_id = ?";

    static final String GET_ALL = "SELECT * " +
            "FROM genre " +
            "ORDER BY genre_id";

    static final String GET_ALL_GENRES_BY_FILM_ID = "SELECT fg.genre_id AS genre_id, " +
            "g.name AS name " +
            "FROM film_genre AS fg " +
            "INNER JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "WHERE fg.film_id = ?";
}
