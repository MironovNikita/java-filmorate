package ru.yandex.practicum.filmorate.likeTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(statements = "DELETE FROM film")
@Sql(statements = "ALTER TABLE film ALTER COLUMN film_id RESTART WITH 1")
@Sql(statements = "DELETE FROM users")
@Sql(statements = "ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1")
public class LikeDbStorageTest {
    private final LikeDbStorage likeDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;

    @DisplayName("Проверка метода постановки лайка фильму")
    @Test
    void shouldPutLikeToFilmFromUser() {
        Film film1 = createTestFilm(1);
        Film film2 = createTestFilm(2).withName("Fast and Furious 2");
        filmDbStorage.create(film1);
        filmDbStorage.create(film2);

        User user = createTestUser(1);
        userDbStorage.create(user);

        Assertions.assertEquals(likeDbStorage.getMostLikedFilms(2).get(0), film1);
        likeDbStorage.likeFilm(film2.getId(), user.getId());
        Assertions.assertEquals(likeDbStorage.getMostLikedFilms(2).get(0), film2);
    }

    @DisplayName("Проверка метода получения списка наиболее популярных фильмов")
    @Test
    void shouldReturnMostPopularFilmList() {
        Film film1 = createTestFilm(1);
        Film film2 = createTestFilm(2).withName("Fast and Furious 2");
        Film film3 = createTestFilm(3).withName("Fast and Furious 3");
        filmDbStorage.create(film1);
        filmDbStorage.create(film2);
        filmDbStorage.create(film3);

        User user1 = createTestUser(1);
        User user2 = createTestUser(2).withName("Name2").withEmail("test2@yandex.ru").withLogin("Login2");
        userDbStorage.create(user1);
        userDbStorage.create(user2);

        likeDbStorage.likeFilm(film2.getId(), user1.getId());
        likeDbStorage.likeFilm(film1.getId(), user2.getId());
        likeDbStorage.likeFilm(film2.getId(), user2.getId());

        List<Film> mostLikedFilms = List.of(film2, film1, film3);
        Assertions.assertEquals(likeDbStorage.getMostLikedFilms(3), mostLikedFilms);
    }

    @DisplayName("Проверка метода удаления лайка у фильма")
    @Test
    void shouldDeleteLikeToFilmFromUser() {
        Film film1 = createTestFilm(1);
        Film film2 = createTestFilm(2).withName("Fast and Furious 2");
        filmDbStorage.create(film1);
        filmDbStorage.create(film2);

        User user = createTestUser(1);
        userDbStorage.create(user);

        Assertions.assertEquals(likeDbStorage.getMostLikedFilms(2).get(0), film1);
        likeDbStorage.likeFilm(film2.getId(), user.getId());
        Assertions.assertEquals(likeDbStorage.getMostLikedFilms(2).get(0), film2);
        likeDbStorage.deleteLikeFromFilm(film2.getId(), user.getId());
        Assertions.assertEquals(likeDbStorage.getMostLikedFilms(2).get(0), film1);
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
