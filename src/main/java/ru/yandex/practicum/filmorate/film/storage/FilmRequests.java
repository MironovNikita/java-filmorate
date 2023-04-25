package ru.yandex.practicum.filmorate.film.storage;

public class FilmRequests {
    static final String UPDATE = "UPDATE film " +
            "SET name = ?, " +
            "description = ?, " +
            "release_date = ?, " +
            "duration = ?, " +
            "rating_id = ? " +
            "WHERE film_id = ?";

    static final String GET_BY_ID = "SELECT * " +
            "FROM film AS f " +
            "INNER JOIN mpa AS m ON f.rating_id = m.rating_id " +
            "WHERE f.film_id = ?";

    static final String GET_ALL = "SELECT * " +
            "FROM film AS f " +
            "INNER JOIN mpa AS m ON f.rating_id = m.rating_id";

    static final String DELETE = "DELETE " +
            "FROM film " +
            "WHERE film_id = ?";

    static final String ADD_GENRE = "INSERT INTO film_genre VALUES(?, ?)";

    static final String DELETE_FILM_GENRE = "DELETE FROM film_genre WHERE film_id = ?";
}
