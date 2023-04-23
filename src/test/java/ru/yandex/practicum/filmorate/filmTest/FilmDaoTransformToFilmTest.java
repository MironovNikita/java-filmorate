package ru.yandex.practicum.filmorate.filmTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.film.dao.FilmDao;
import ru.yandex.practicum.filmorate.film.dao.FilmDaoTransformToFilm;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.time.LocalDate;
import java.util.Collections;

public class FilmDaoTransformToFilmTest {
    private final FilmDaoTransformToFilm filmDaoTransformToFilm = new FilmDaoTransformToFilm();

    @DisplayName("Проверка маппинга FilmDao -> Film")
    @Test
    void filmDaoToFilmTransformation() {
        FilmDao filmDao = new FilmDao (
                1L,
                "Fast and Furious",
                "The Fast and the Furious",
                LocalDate.of(2000,6,18),
                106,
                new Mpa(3, "PG-13"),
                Collections.emptyList()
        );

        Film film = filmDaoTransformToFilm.transformFrom(filmDao);

        Assertions.assertEquals(filmDao.getId(), film.getId());
        Assertions.assertEquals(filmDao.getName(), film.getName());
        Assertions.assertEquals(filmDao.getDescription(), film.getDescription());
        Assertions.assertEquals(filmDao.getDescription(), film.getDescription());
        Assertions.assertEquals(filmDao.getReleaseDate(), film.getReleaseDate());
        Assertions.assertEquals(filmDao.getDuration(), film.getDuration());
        Assertions.assertEquals(filmDao.getGenres(), film.getGenres());
        Assertions.assertEquals(filmDao.getMpa(), film.getMpa());
    }
}
