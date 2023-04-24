package ru.yandex.practicum.filmorate.filmTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.film.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.like.storage.LikeDbStorage;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(statements = "DELETE FROM film")
@Sql(statements = "ALTER TABLE film ALTER COLUMN film_id RESTART WITH 1")
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    @DisplayName("Проверка метода создания фильма")
    @Test
    void shouldCreateFilm() {
        Film film1 = createTestFilm(1);
        filmDbStorage.create(film1);
        Assertions.assertEquals(film1, filmDbStorage.getById(1));

        Film film2 = createTestFilm(2);
        filmDbStorage.create(film2);
        Assertions.assertEquals(film2, filmDbStorage.getById(2));

        assertThat(filmDbStorage.getAll().size()).isEqualTo(2);
    }

    @DisplayName("Проверка метода обновления фильма")
    @Test
    void shouldUpdateFilm() {
        Film film = createTestFilm(1);
        Film filmAsUpdate = createTestFilm(1).withName("Fast and Fastest").withDuration(1000)
                .withDescription("Pirate copy of the legendary film");
        filmDbStorage.create(film);
        filmDbStorage.update(filmAsUpdate.getId(), filmAsUpdate);

        assertThat(filmDbStorage.getById(1)).isEqualTo(filmAsUpdate);
    }

    @DisplayName("Проверка метода обновления фильма при передаче несуществующего id")
    @Test
    void shouldNotUpdateFilmIfIdDoesNotExist() {
        Film film = createTestFilm(1);
        Film filmAsUpdate = createTestFilm(12).withName("Fast and Fastest").withDuration(1000)
                .withDescription("Pirate copy of the legendary film");
        filmDbStorage.create(film);

        assertThat(filmDbStorage.update(filmAsUpdate.getId(), filmAsUpdate)).isNull();
        assertThat(filmDbStorage.getById(1)).isEqualTo(film);
        assertThat(filmDbStorage.getById(1)).isNotEqualTo(filmAsUpdate);
    }

    @DisplayName("Проверка метода получения фильма по id")
    @Test
    void shouldReturnFilmById() {
        Film film1 = createTestFilm(1);
        Film film2 = createTestFilm(2).withName("Fast and Furious 2");
        Film film3 = createTestFilm(3).withName("Fast and Furious 3");

        filmDbStorage.create(film1);
        filmDbStorage.create(film2);
        filmDbStorage.create(film3);

        assertThat(filmDbStorage.getById(1)).isEqualTo(film1);
        assertThat(filmDbStorage.getById(2)).isEqualTo(film2);
        assertThat(filmDbStorage.getById(3)).isEqualTo(film3);
    }

    @DisplayName("Проверка метода получения фильма по несуществующему id")
    @Test
    void shouldReturnFilmByIdIfIdDoesNotExist() {
        Film film1 = createTestFilm(1);
        Film film2 = createTestFilm(2).withName("Fast and Furious 2");

        filmDbStorage.create(film1);
        filmDbStorage.create(film2);

        assertThat(filmDbStorage.getAll().size()).isEqualTo(2);
        assertThat(filmDbStorage.getById(3000)).isNull();
    }

    @DisplayName("Проверка метода получения всех фильмов")
    @Test
    void shouldReturnAllFilms() {
        Film film1 = createTestFilm(1);
        Film film2 = createTestFilm(2).withName("Fast and Furious 2");
        Film film3 = createTestFilm(3).withName("Fast and Furious 3");

        filmDbStorage.create(film1);
        filmDbStorage.create(film2);
        filmDbStorage.create(film3);

        List<Film> filmCheckList = List.of(film1, film2, film3);
        Assertions.assertEquals(filmCheckList, filmDbStorage.getAll());
    }

    @DisplayName("Проверка метода получения всех фильмов при пустом списке")
    @Test
    void shouldNotReturnAllFilmsBecauseOfTableFilmsIsEmpty() {
        assertThat(filmDbStorage.getAll().size()).isEqualTo(0);
    }

    @DisplayName("Проверка метода удаления фильма по id")
    @Test
    void shouldDeleteFilmById() {
        Film film1 = createTestFilm(1);
        Film film2 = createTestFilm(2).withName("Fast and Furious 2");
        Film film3 = createTestFilm(3).withName("Fast and Furious 3");

        filmDbStorage.create(film1);
        filmDbStorage.create(film2);
        filmDbStorage.create(film3);

        assertThat(filmDbStorage.getAll().size()).isEqualTo(3);
        assertThat(filmDbStorage.delete(1)).isEqualTo(film1);
        assertThat(filmDbStorage.getAll().size()).isEqualTo(2);
    }

    @DisplayName("Проверка метода удаления фильма по несуществующему id")
    @Test
    void shouldNotDeleteAnyFilmByNonExistentId() {
        Film film1 = createTestFilm(1);
        Film film2 = createTestFilm(2).withName("Fast and Furious 2");

        filmDbStorage.create(film1);
        filmDbStorage.create(film2);

        assertThat(filmDbStorage.getAll().size()).isEqualTo(2);
        assertThat(filmDbStorage.delete(213)).isNull();
        assertThat(filmDbStorage.getAll().size()).isEqualTo(2);
    }

    private Film createTestFilm(long id) {
        return new Film(
                id,
                "Fast and Furious",
                "The Fast and the Furious",
                LocalDate.of(2000, 6, 18),
                106,
                new Mpa(3, "PG-13"),
                new LinkedHashSet<>());
    }

    private User createTestUser(long id) {
        return new User(
                id,
                "test@yandex.ru",
                "Login",
                "Name",
                LocalDate.of(1990, 10, 10));
    }
}



