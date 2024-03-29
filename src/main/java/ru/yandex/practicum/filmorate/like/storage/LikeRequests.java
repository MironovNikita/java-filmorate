package ru.yandex.practicum.filmorate.like.storage;

public class LikeRequests {
    static final String GET_MOST_LIKED_FILMS = "SELECT f.film_id, " +
            "f.name, " +
            "f.description, " +
            "f.release_date, " +
            "f.duration, " +
            "m.rating_id AS rating_id, " +
            "m.name AS mpa_name, " +
            "g.genre_id, " +
            "g.name AS genre_name, " +
            "COUNT(l.film_id) AS likes " +
            "FROM film AS f " +
            "INNER JOIN mpa AS m ON f.rating_id = m.rating_id " +
            "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
            "GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration, m.rating_id, m.name," +
            "g.genre_id, g.name " +
            "ORDER BY likes DESC, f.name " +
            "LIMIT ?";

    static final String LIKE_FILM = "INSERT INTO likes VALUES(?, ?)";

    static final String DELETE_LIKE_FROM_FILM = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
}
