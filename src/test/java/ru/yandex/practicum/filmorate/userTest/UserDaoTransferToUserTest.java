package ru.yandex.practicum.filmorate.userTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.user.dao.UserDao;
import ru.yandex.practicum.filmorate.user.dao.UserDaoTransformToUser;
import ru.yandex.practicum.filmorate.user.model.User;

import java.time.LocalDate;

public class UserDaoTransferToUserTest {
    UserDaoTransformToUser userDaoTransformToUser = new UserDaoTransformToUser();

    @DisplayName("Проверка маппинга UserDao -> User при заполненном имени")
    @Test
    void userDaoToFilmTransformationWithFilledName() {
        UserDao userDao = new UserDao(
                1L,
                "email@yandex.ru",
                "login",
                "name",
                LocalDate.of(2000,10,10)
        );

        User user = userDaoTransformToUser.transformFrom(userDao);

        Assertions.assertEquals(userDao.getId(), user.getId());
        Assertions.assertEquals(userDao.getEmail(), user.getEmail());
        Assertions.assertEquals(userDao.getLogin(), user.getLogin());
        Assertions.assertEquals(userDao.getName(), user.getName());
        Assertions.assertEquals(userDao.getBirthday(), user.getBirthday());
    }

    @DisplayName("Проверка маппинга UserDao -> User при незаполненном имени")
    @Test
    void userDaoToFilmTransformationWithNotFilledName() {
        UserDao userDao = new UserDao(
                1L,
                "testemail@yandex.ru",
                "login",
                null,
                LocalDate.of(2000,10,10)
        );

        User user = userDaoTransformToUser.transformFrom(userDao);

        Assertions.assertEquals(userDao.getId(), user.getId());
        Assertions.assertEquals(userDao.getEmail(), user.getEmail());
        Assertions.assertEquals(userDao.getLogin(), user.getLogin());
        Assertions.assertEquals(userDao.getLogin(), user.getName());
        Assertions.assertEquals(userDao.getBirthday(), user.getBirthday());
    }
}
