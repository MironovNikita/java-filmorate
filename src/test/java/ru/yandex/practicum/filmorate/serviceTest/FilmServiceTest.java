package ru.yandex.practicum.filmorate.serviceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storageInMemory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storageInMemory.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilmServiceTest {
    FilmStorage filmStorage;
    UserStorage userStorage;
    FilmService filmService;


    @BeforeEach
    void createData(){
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
    }

    Film setFilmParams() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDescription("FilmDescription");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(1995,8,14));
        filmService.checkFilmLikesList(film);
        return film;
    }

    User setUserParams() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setLogin("testUser");
        user.setName("UserName");
        user.setBirthday(LocalDate.of(1995,8,14));
        return user;
    }

    @DisplayName("Проверка создания списка лайков у фильма без них")
    @Test
    void checkFilmLikesList() {
        Film film = new Film();
        film.setName("FilmName");
        film.setDescription("FilmDescription");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(1995,8,14));
        filmStorage.create(film);
        Assertions.assertNull(film.getLikes());
        filmService.checkFilmLikesList(film);
        Assertions.assertNotNull(film.getLikes());
    }

    @DisplayName("Проверка установки лайка на фильм")
    @Test
    void checkLikeIsAdded() {
        Film film1 = setFilmParams();
        Film film2 = setFilmParams();
        User user1 = setUserParams();
        filmStorage.create(film1);
        filmStorage.create(film2);
        userStorage.create(user1);
        filmService.likeFilm(film1.getId(), user1.getId());
        Assertions.assertEquals(1, film1.getLikes().size());
        User user2 = setUserParams();
        userStorage.create(user2);
        filmService.likeFilm(film1.getId(), user2.getId());
        filmService.likeFilm(film2.getId(), user2.getId());
        Assertions.assertEquals(2, film1.getLikes().size());
        Assertions.assertEquals(1, film2.getLikes().size());
    }

    @DisplayName("Проверка удаления лайка с фильма")
    @Test
    void checkFilmLikeDeletion() {
        Film film1 = setFilmParams();
        User user1 = setUserParams();
        User user2 = setUserParams();
        User user3 = setUserParams();
        filmStorage.create(film1);
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        filmService.likeFilm(film1.getId(), user1.getId());
        filmService.likeFilm(film1.getId(), user2.getId());
        filmService.likeFilm(film1.getId(), user3.getId());
        filmService.likeFilm(film1.getId(), user3.getId());
        Assertions.assertEquals(3, film1.getLikes().size());
        filmService.deleteLikeFromFilm(film1.getId(), user2.getId());
        Assertions.assertEquals(2, film1.getLikes().size());
    }

    @DisplayName("Проверка постановки лайка несуществующего пользователя/фильма")
    @Test
    void shouldThrowObjectNotFoundExceptionIfLiking() {
        Film film1 = setFilmParams();
        User user1 = setUserParams();
        User user2 = setUserParams();
        filmStorage.create(film1);
        userStorage.create(user1);
        userStorage.create(user2);
        filmService.likeFilm(film1.getId(), user1.getId());
        filmService.likeFilm(film1.getId(), user2.getId());
        ObjectNotFoundException exception1 = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> filmService.likeFilm(20, user1.getId())
        );
        Assertions.assertTrue(exception1.getMessage().contentEquals("Фильм с id: 20 не найден!"));
        ObjectNotFoundException exception2 = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> filmService.likeFilm(film1.getId(), 50)
        );
        Assertions.assertTrue(exception2.getMessage().contentEquals("Пользователь с id: 50 не найден!"));
    }

    @DisplayName("Проверка удаления лайка несуществующего пользователя/фильма")
    @Test
    void shouldThrowObjectNotFoundExceptionIfDeletingLike() {
        Film film1 = setFilmParams();
        User user1 = setUserParams();
        User user2 = setUserParams();
        filmStorage.create(film1);
        userStorage.create(user1);
        userStorage.create(user2);
        filmService.likeFilm(film1.getId(), user1.getId());
        filmService.likeFilm(film1.getId(), user2.getId());
        ObjectNotFoundException exception1 = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> filmService.deleteLikeFromFilm(20, user1.getId())
        );
        Assertions.assertTrue(exception1.getMessage().contentEquals("Фильм с id: 20 не найден!"));
        ObjectNotFoundException exception2 = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> filmService.deleteLikeFromFilm(film1.getId(), 50)
        );
        Assertions.assertTrue(exception2.getMessage().contentEquals("Пользователь с id: 50 не найден!"));
    }

    @Test
    @DisplayName("Проверка возврата списка самых лучших фильмов")
    void shouldReturnMostLikedFilms() {
        Film film1 = setFilmParams();
        Film film2 = setFilmParams();
        Film film3 = setFilmParams();
        User user1 = setUserParams();
        User user2 = setUserParams();
        User user3 = setUserParams();
        filmStorage.create(film1);
        filmStorage.create(film2);
        filmStorage.create(film3);
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        filmService.likeFilm(film3.getId(), user1.getId());
        filmService.likeFilm(film3.getId(), user2.getId());
        filmService.likeFilm(film3.getId(), user3.getId());
        filmService.likeFilm(film1.getId(), user3.getId());
        List<Film> filmsListCheck = new ArrayList<>();
        filmsListCheck.add(film3);
        filmsListCheck.add(film1);
        filmsListCheck.add(film2);
        Assertions.assertEquals(filmService.getMostLikedFilms(10), filmsListCheck);
    }
}
