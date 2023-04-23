package ru.yandex.practicum.filmorate.userTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(statements = "DELETE FROM users")
@Sql(statements = "ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1")
@Sql(statements = "DELETE FROM likes")
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @DisplayName("Проверка метода создания пользователя")
    @Test
    void shouldCreateUser() {
        User user1 = createTestUser(1);
        userDbStorage.create(user1);
        Assertions.assertEquals(user1, userDbStorage.getById(1));

        User user2 = createTestUser(2).withName("Name2").withEmail("test2@yandex.ru").withLogin("Login2");
        userDbStorage.create(user2);
        Assertions.assertEquals(user2, userDbStorage.getById(2));

        assertThat(userDbStorage.getAll().size()).isEqualTo(2);
    }

    @DisplayName("Проверка метода обновления пользователя")
    @Test
    void shouldUpdateUser() {
        User user = createTestUser(1);
        User userAsUpdate = createTestUser(1).withName("Name2").withEmail("test2@yandex.ru").withLogin("Login2");
        userDbStorage.create(user);
        userDbStorage.update(userAsUpdate.getId(), userAsUpdate);

        assertThat(userDbStorage.getById(1)).isEqualTo(userAsUpdate);
    }

    @DisplayName("Проверка метода обновления пользователя при передаче несуществующего id")
    @Test
    void shouldNotUpdateUserIfIdDoesNotExist() {
        User user = createTestUser(1);
        User userAsUpdate = createTestUser(12).withName("Name2").withEmail("test2@yandex.ru").withLogin("Login2");
        userDbStorage.create(user);

        assertThat(userDbStorage.update(userAsUpdate.getId(), userAsUpdate)).isNull();
        assertThat(userDbStorage.getById(1)).isEqualTo(user);
        assertThat(userDbStorage.getById(1)).isNotEqualTo(userAsUpdate);
    }

    @DisplayName("Проверка метода получения пользователя по id")
    @Test
    void shouldReturnUserById() {
        User user1 = createTestUser(1);
        User user2 = createTestUser(2).withName("Name2").withEmail("test2@yandex.ru").withLogin("Login2");
        User user3 = createTestUser(3).withName("Name3").withEmail("test3@yandex.ru").withLogin("Login3");

        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.create(user3);

        assertThat(userDbStorage.getById(1)).isEqualTo(user1);
        assertThat(userDbStorage.getById(2)).isEqualTo(user2);
        assertThat(userDbStorage.getById(3)).isEqualTo(user3);
    }

    @DisplayName("Проверка метода получения пользователя по несуществующему id")
    @Test
    void shouldReturnUserByIdIfIdDoesNotExist() {
        User user1 = createTestUser(1);
        User user2 = createTestUser(2).withName("Name2").withEmail("test2@yandex.ru").withLogin("Login2");

        userDbStorage.create(user1);
        userDbStorage.create(user2);

        assertThat(userDbStorage.getAll().size()).isEqualTo(2);
        assertThat(userDbStorage.getById(3000)).isNull();
    }

    @DisplayName("Проверка метода получения пользователя по email")
    @Test
    void shouldReturnUserByEmail() {
        User user = createTestUser(1);
        userDbStorage.create(user);

        assertThat(userDbStorage.getByEmail("test@yandex.ru")).isEqualTo(user);
    }

    @DisplayName("Проверка метода получения пользователя по несуществующему email")
    @Test
    void shouldReturnUserByNonExistentEmail() {
        User user = createTestUser(1);
        userDbStorage.create(user);

        assertThat(userDbStorage.getByEmail("smth@yandex.ru")).isNull();
    }

    @DisplayName("Проверка метода получения всех пользователей")
    @Test
    void shouldReturnAllUsers() {
        User user1 = createTestUser(1);
        User user2 = createTestUser(2).withName("Name2").withEmail("test2@yandex.ru").withLogin("Login2");
        User user3 = createTestUser(3).withName("Name3").withEmail("test3@yandex.ru").withLogin("Login3");

        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.create(user3);

        List<User> userCheckList = List.of(user1, user2, user3);
        Assertions.assertEquals(userCheckList, userDbStorage.getAll());
    }

    @DisplayName("Проверка метода получения всех пользователей при пустом списке")
    @Test
    void shouldNotReturnAllUsersBecauseOfTableUsersIsEmpty() {
        assertThat(userDbStorage.getAll().size()).isEqualTo(0);
    }

    @DisplayName("Проверка метода удаления пользователя по id")
    @Test
    void shouldDeleteUserById() {
        User user1 = createTestUser(1);
        User user2 = createTestUser(2).withName("Name2").withEmail("test2@yandex.ru").withLogin("Login2");
        User user3 = createTestUser(3).withName("Name3").withEmail("test3@yandex.ru").withLogin("Login3");

        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.create(user3);

        assertThat(userDbStorage.getAll().size()).isEqualTo(3);
        assertThat(userDbStorage.delete(1)).isEqualTo(user1);
        assertThat(userDbStorage.getAll().size()).isEqualTo(2);
    }

    @DisplayName("Проверка метода удаления пользователя по несуществующему id")
    @Test
    void shouldNotDeleteAnyUserByNonExistentId() {
        User user1 = createTestUser(1);
        User user2 = createTestUser(2).withName("Name2").withEmail("test2@yandex.ru").withLogin("Login2");

        userDbStorage.create(user1);
        userDbStorage.create(user2);

        assertThat(userDbStorage.getAll().size()).isEqualTo(2);
        assertThat(userDbStorage.delete(213)).isNull();
        assertThat(userDbStorage.getAll().size()).isEqualTo(2);
    }

    @DisplayName("Проверка добавления друга и получения списка всех друзей")
    @Test
    void shouldAddFriendById() {
        User user1 = createTestUser(1);
        User user2 = createTestUser(2).withName("Name2").withEmail("test2@yandex.ru").withLogin("Login2");
        User user3 = createTestUser(3).withName("Name3").withEmail("test3@yandex.ru").withLogin("Login3");

        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.create(user3);

        userDbStorage.addFriend(user1.getId(), user2.getId());
        userDbStorage.addFriend(user1.getId(), user3.getId());
        List<User> user1CheckFriendList = List.of(user2, user3);

        Assertions.assertEquals(user1CheckFriendList, userDbStorage.getFriends(user1.getId()));
    }

    @DisplayName("Проверка получения списка общих друзей")
    @Test
    void shouldReturnCommonFriendList() {
        User user1 = createTestUser(1);
        User user2 = createTestUser(2).withName("Name2").withEmail("test2@yandex.ru").withLogin("Login2");
        User user3 = createTestUser(3).withName("Name3").withEmail("test3@yandex.ru").withLogin("Login3");
        User user4 = createTestUser(4).withName("Name4").withEmail("test4@yandex.ru").withLogin("Login4");

        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.create(user3);
        userDbStorage.create(user4);

        userDbStorage.addFriend(user1.getId(), user3.getId());
        userDbStorage.addFriend(user1.getId(), user4.getId());
        userDbStorage.addFriend(user2.getId(), user3.getId());
        userDbStorage.addFriend(user2.getId(), user4.getId());
        List<User> user1And2CheckCommonFriendList = List.of(user3, user4);

        Assertions.assertEquals(user1And2CheckCommonFriendList,
                userDbStorage.getCommonFriends(user1.getId(), user2.getId()));
    }

    @DisplayName("Проверка удаления друга")
    @Test
    void shouldDeleteFriendById() {
        User user1 = createTestUser(1);
        User user2 = createTestUser(2).withName("Name2").withEmail("test2@yandex.ru").withLogin("Login2");
        User user3 = createTestUser(3).withName("Name3").withEmail("test3@yandex.ru").withLogin("Login3");

        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.create(user3);

        userDbStorage.addFriend(user1.getId(), user2.getId());
        userDbStorage.addFriend(user1.getId(), user3.getId());

        List<User> user1CheckFriendList = List.of(user3);
        userDbStorage.deleteFriend(user1.getId(), user2.getId());

        Assertions.assertEquals(user1CheckFriendList, userDbStorage.getFriends(user1.getId()));
    }

    private User createTestUser(long id) {
        return new User(
                id,
                "test@yandex.ru",
                "Login",
                "Name",
                LocalDate.of(1990,10,10));
    }
}
