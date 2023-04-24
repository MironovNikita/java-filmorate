package ru.yandex.practicum.filmorate.genre.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.genre.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getById(int id) {
        try {
            log.info("Запрошен жанр фильма по id '{}'", id);
            return jdbcTemplate.queryForObject(GenreRequests.GET_BY_ID, this::genreMapping, id);
        } catch (DataAccessException exception) {
            log.warn("Жанр с Id '{}' не найден", id);
            return null;
        }
    }

    @Override
    public List<Genre> getAll() {
        log.info("Запрошен список всех жанров фильмов");
        return jdbcTemplate.query(GenreRequests.GET_ALL, this::genreMapping);
    }

    @Override
    public LinkedHashSet<Genre> getAllGenresByFilmId(long filmId) {
        try {
            log.info("Запрошен список всех жанров фильма с id '{}'", filmId);
            return new LinkedHashSet<>(jdbcTemplate.query(GenreRequests.GET_ALL_GENRES_BY_FILM_ID, this::genreMapping,
                    filmId));
        } catch (DataAccessException exception) {
            log.warn("Жанры для фильма с Id '{}' не найдены", filmId);
            return null;
        }
    }

    private Genre genreMapping(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"), resultSet.getString("name"));
    }
}
