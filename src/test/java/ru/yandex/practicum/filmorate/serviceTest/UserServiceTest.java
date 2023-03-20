package ru.yandex.practicum.filmorate.serviceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storageInMemory.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {
    UserStorage userStorage;
    UserService userService;

    @BeforeEach
    void createData(){
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
    }

    User setUserParams() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setLogin("testUser");
        user.setName("UserName");
        user.setBirthday(LocalDate.of(1995,8,14));
        userService.checkUserFriendList(user);
        return user;
    }

    @DisplayName("Проверка создания списка друзей у пользователя без них")
    @Test
    void checkFilmLikesList() {
        User user = new User();
        user.setName("UserName");
        user.setEmail("test@mail.com");
        user.setLogin("testUser");
        user.setName("UserName");
        user.setBirthday(LocalDate.of(1995,8,14));
        userStorage.create(user);
        Assertions.assertNull(user.getFriends());
        userService.checkUserFriendList(user);
        Assertions.assertNotNull(user.getFriends());
    }

    @DisplayName("Проверка взаимодобавления/взаимоудаления друзей у пользователей")
    @Test
    void checkFriendsAddedAndDeleted() {
        User user1 = setUserParams();
        User user2 = setUserParams();
        User user3 = setUserParams();
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user1.getId(), user2.getId());

        Assertions.assertTrue(user1.getFriends().contains(user3.getId()));
        Assertions.assertTrue(user3.getFriends().contains(user1.getId()));

        userService.deleteFriend(user1.getId(), user3.getId());
        Assertions.assertFalse(user1.getFriends().contains(user3.getId()));
        Assertions.assertFalse(user3.getFriends().contains(user1.getId()));
    }

    @DisplayName("Проверка взаимодобавления/взаимоудаления несуществующих друзей у пользователей")
    @Test
    void checkThrowsObjectNotFoundExceptionIfUserOrFriendIsNonExistent() {
        User user1 = setUserParams();
        User user2 = setUserParams();
        User user3 = setUserParams();
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user1.getId(), user2.getId());

        ObjectNotFoundException exception1 = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> userService.addFriend(20, user1.getId())
        );
        Assertions.assertTrue(exception1.getMessage().contentEquals("Пользователь с id: 20 не найден!"));
        ObjectNotFoundException exception2 = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> userService.deleteFriend(user3.getId(), 100)
        );
        Assertions.assertTrue(exception2.getMessage().contentEquals("Пользователь с id: 100 не найден!"));
    }

    @DisplayName("Проверка возвращения списка общих друзей")
    @Test
    void shouldReturnCommonFriendsList() {
        User user1 = setUserParams();
        User user2 = setUserParams();
        User user3 = setUserParams();
        User user4 = setUserParams();
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        userStorage.create(user4);

        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user1.getId(), user4.getId());
        userService.addFriend(user3.getId(), user2.getId());
        userService.addFriend(user3.getId(), user4.getId());

        List<User> checkCommonFriendList = userService.getCommonFriends(user1.getId(), user3.getId());
        List<User> verifyCommonFriendList = new ArrayList<>();
        verifyCommonFriendList.add(user2);
        verifyCommonFriendList.add(user4);

        Assertions.assertEquals(verifyCommonFriendList, checkCommonFriendList);
    }

    @DisplayName("Проверка получения списка друзей пользователя")
    @Test
    void shouldReturnUserFriendList() {
        User user1 = setUserParams();
        User user2 = setUserParams();
        User user3 = setUserParams();
        User user4 = setUserParams();
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        userStorage.create(user4);

        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user1.getId(), user4.getId());

        List<User> verifyFriendList = new ArrayList<>();
        verifyFriendList.add(user2);
        verifyFriendList.add(user3);
        verifyFriendList.add(user4);
        Assertions.assertEquals(verifyFriendList, userService.getFriends(user1.getId()));
    }
}
