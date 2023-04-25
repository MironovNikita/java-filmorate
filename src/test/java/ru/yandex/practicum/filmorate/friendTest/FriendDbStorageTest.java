package ru.yandex.practicum.filmorate.friendTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.friend.storage.FriendDbStorage;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(statements = "DELETE FROM users")
@Sql(statements = "ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1")
@Sql(statements = "DELETE FROM likes")
public class FriendDbStorageTest {
    private final FriendDbStorage friendDbStorage;
    private final UserDbStorage userDbStorage;

    @DisplayName("Проверка добавления друга и получения списка всех друзей")
    @Test
    void shouldAddFriendById() {
        User user1 = createTestUser(1);
        User user2 = createTestUser(2).withName("Name2").withEmail("test2@yandex.ru").withLogin("Login2");
        User user3 = createTestUser(3).withName("Name3").withEmail("test3@yandex.ru").withLogin("Login3");

        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.create(user3);

        friendDbStorage.addFriend(user1.getId(), user2.getId());
        friendDbStorage.addFriend(user1.getId(), user3.getId());
        List<User> user1CheckFriendList = List.of(user2, user3);

        Assertions.assertEquals(user1CheckFriendList, friendDbStorage.getFriends(user1.getId()));
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

        friendDbStorage.addFriend(user1.getId(), user3.getId());
        friendDbStorage.addFriend(user1.getId(), user4.getId());
        friendDbStorage.addFriend(user2.getId(), user3.getId());
        friendDbStorage.addFriend(user2.getId(), user4.getId());
        List<User> user1And2CheckCommonFriendList = List.of(user3, user4);

        Assertions.assertEquals(user1And2CheckCommonFriendList,
                friendDbStorage.getCommonFriends(user1.getId(), user2.getId()));
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

        friendDbStorage.addFriend(user1.getId(), user2.getId());
        friendDbStorage.addFriend(user1.getId(), user3.getId());

        List<User> user1CheckFriendList = List.of(user3);
        friendDbStorage.deleteFriend(user1.getId(), user2.getId());

        Assertions.assertEquals(user1CheckFriendList, friendDbStorage.getFriends(user1.getId()));
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
