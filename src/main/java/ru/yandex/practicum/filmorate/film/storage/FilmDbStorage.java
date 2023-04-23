package ru.yandex.practicum.filmorate.film.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.genre.storage.GenreStorage;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("film")
                .usingGeneratedKeyColumns("film_id");
        Map<String, Object> filmTable = new HashMap<>();
        filmTable.put("name", film.getName());
        filmTable.put("description", film.getDescription());
        filmTable.put("release_date", film.getReleaseDate());
        filmTable.put("duration", film.getDuration());
        filmTable.put("rating_id", film.getMpa().getId());

        long filmId = simpleJdbcInsert.executeAndReturnKey(filmTable).longValue();

        checkFilmGenres(film.getGenres(), filmId);
        log.info("Запрос на создание фильма с названием '{}'", film.getName());
        return getById(filmId);
    }

    @Override
    public Film update(long id, Film film) {
        jdbcTemplate.update(FilmRequests.UPDATE, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), id);

        jdbcTemplate.update(FilmRequests.DELETE_FILM_GENRE, film.getId());
        checkFilmGenres(film.getGenres(), id);
        log.info("Запрос на обновление фильма с id '{}'", id);
        return getById(id);
    }

    @Override
    public Film getById(long id) {
        try {
            log.info("Запрошен поиск фильма по id '{}'", id);
            return jdbcTemplate.queryForObject(FilmRequests.GET_BY_ID, this::filmMapping, id);
        } catch (DataAccessException exception) {
            log.warn("Фильм с id '{}' не найден", id);
            return null;
        }
    }

    @Override
    public List<Film> getAll() {
        log.info("Запрошен список всех фильмов");
        return jdbcTemplate.query(FilmRequests.GET_ALL, this::filmMapping);
    }

    @Override
    public Film delete(long id) {
        Film filmToDelete = getById(id);
        if(filmToDelete == null) {
            log.info("Запрос на удаление фильма не выполнен. Фильм с id '{}' отсутствует", id);
            return null;
        }
        jdbcTemplate.update(FilmRequests.DELETE, id);
        log.info("Запрос на удаление фильма с id '{}' выполнен", id);
        return filmToDelete;
    }

    @Override
    public List<Film> getMostLikedFilms(int quantity) {
        log.info("Запрошен список фильмов с наибольшим количеством лайков");
        return jdbcTemplate.query(FilmRequests.GET_MOST_LIKED_FILMS, this::filmMapping, Math.max(quantity, 0));
    }

    @Override
    public void likeFilm(long filmId, long userId) {
        log.info("Запрос поставить лайк фильму с id '{}' пользователем с id '{}'", filmId, userId);
        jdbcTemplate.update(FilmRequests.LIKE_FILM, filmId, userId);
    }

    @Override
    public void deleteLikeFromFilm(long filmId, long userId) {
        log.info("Запрос убрать лайк фильма с id '{}' пользователем с id '{}'", filmId, userId);
        jdbcTemplate.update(FilmRequests.DELETE_LIKE_FROM_FILM, filmId, userId);
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

    private void checkFilmGenres(List<Genre> genres, long filmId) {
        if(genres == null) {
            return;
        }
        List<Integer> genreIds = genres.stream().map(Genre::getId).distinct().collect(Collectors.toList());

        jdbcTemplate.batchUpdate(FilmRequests.ADD_GENRE, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                int genreId = genreIds.get(i);
                ps.setLong(1, filmId);
                ps.setInt(2, genreId);
            }

            @Override
            public int getBatchSize() {
                return genreIds.size();
            }
        });
    }
}
