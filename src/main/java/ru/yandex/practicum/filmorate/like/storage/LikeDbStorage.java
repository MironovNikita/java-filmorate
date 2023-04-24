package ru.yandex.practicum.filmorate.like.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.genre.storage.GenreStorage;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository("likeDbStorage")
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    private final GenreStorage genreStorage;

    @Override
    public List<Film> getMostLikedFilms(int quantity) {
        log.info("Запрошен список фильмов с наибольшим количеством лайков");
        return jdbcTemplate.query(LikeRequests.GET_MOST_LIKED_FILMS, this::filmMapping, Math.max(quantity, 0));
    }

    @Override
    public void likeFilm(long filmId, long userId) {
        log.info("Запрос поставить лайк фильму с id '{}' пользователем с id '{}'", filmId, userId);
        jdbcTemplate.update(LikeRequests.LIKE_FILM, filmId, userId);
    }

    @Override
    public void deleteLikeFromFilm(long filmId, long userId) {
        log.info("Запрос убрать лайк фильма с id '{}' пользователем с id '{}'", filmId, userId);
        jdbcTemplate.update(LikeRequests.DELETE_LIKE_FROM_FILM, filmId, userId);
    }

    private Film filmMapping(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Film(resultSet.getLong("film_id"),
                resultSet.getString("film.name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getInt("duration"),
                new Mpa(resultSet.getInt("mpa.rating_id"), resultSet.getString("mpa.name")),
                genreStorage.getAllGenresByFilmId(resultSet.getInt("film_id")));
    }
}
