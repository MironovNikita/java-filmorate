package ru.yandex.practicum.filmorate.mpa.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Mpa getById(int id) {
        try {
            log.info("Запрошен жанр по Id '{}'", id);
            return jdbcTemplate.queryForObject(MpaRequests.GET_BY_ID, this::mpaMapping, id);
        } catch (DataAccessException exception) {
            log.warn("Жанр с Id '{}' не найден", id);
            return null;
        }

    }

    @Override
    public List<Mpa> getAll() {
        log.info("Запрошен список всех жанров");
        return jdbcTemplate.query(MpaRequests.GET_ALL, this::mpaMapping);
    }

    private Mpa mpaMapping(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Mpa(resultSet.getInt("rating_id"), resultSet.getString("name"));
    }
}
