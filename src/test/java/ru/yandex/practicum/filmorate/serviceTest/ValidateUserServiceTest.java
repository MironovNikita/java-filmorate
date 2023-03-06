package ru.yandex.practicum.filmorate.serviceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.time.LocalDate;

public class ValidateServiceTest {

    @DisplayName("Проверка валидации пользователя с пустым email или email без @")
    @Test
    void shouldThrowValidationExceptionIfUserEmailIncorrect() {
        ValidateService validateService = new ValidateService();
        User user = new User("userEmail@example.com", "user1", LocalDate.of(1995, 8,
                14));
        user.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> validateService.validateUser(user));
        user.setEmail("userEmailexample.com");
        Assertions.assertThrows(ValidationException.class, () -> validateService.validateUser(user));
    }

    @DisplayName("Проверка валидации пользователя с пустым логином")
    @Test
    void shouldThrowValidationExceptionIfUserHasEmptyLogin() {
        ValidateService validateService = new ValidateService();
        User user = new User("userEmail@example.com", "", LocalDate.of(1995, 8,
                14));
        Assertions.assertThrows(ValidationException.class, () -> validateService.validateUser(user));
    }

    @DisplayName("Проверка валидации пользователя с пустым именем или если его имя null")
    @Test
    void shouldThrowExceptionIfUserNameNullOrMakeItSameAsLogin() {
        ValidateService validateService = new ValidateService();
        User user = new User("userEmail@example.com", "user1", LocalDate.of(1995, 8,
                14));
        Assertions.assertNull(null, user.getName());
        validateService.validateUser(user);
        Assertions.assertEquals(user.getLogin(), user.getName());
        user.setName("          ");
        validateService.validateUser(user);
        Assertions.assertEquals(user.getLogin(), user.getName());
    }

    @DisplayName("Проверка валидации пользователя с днём рождения после текущей даты")
    @Test
    void shouldThrowValidateExceptionIfUserBirthdayIsAfterNowDate() {
        ValidateService validateService = new ValidateService();
        User user = new User("userEmail@example.com", "user1", LocalDate.of(2995, 8,
                14));
        Assertions.assertThrows(ValidationException.class, () -> validateService.validateUser(user));
    }
}
