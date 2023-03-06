package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.data.UserRepository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidateUserService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final ValidateUserService userValidateService = new ValidateUserService();
    private final UserRepository userRepository = new UserRepository();

    @PostMapping()
    public User create(@RequestBody User user) {
        user = userValidateService.validateUser(user);
        userRepository.saveUser(user);
        log.info("Пользователь '{}' с id '{}' был успешно добавлен.", user.getName(), user.getId());
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        if(!userRepository.getUsers().containsKey(user.getId())) {
            log.warn("Запрос на обновление пользователя с id '{}' отклонён. Он отсутствует в списке пользователей.",
                    user.getId());
            throw new RuntimeException("Список пользователей не содержит такого ID");
        }
        user = userValidateService.validateUser(user);
        userRepository.getUsers().put(user.getId(), user);
        log.info("Пользователь '{}' с id '{}' был успешно обновлён.", user.getName(), user.getId());
        return user;
    }

    @GetMapping
    public Collection<User> showAllUsersList() {
        log.info("Запрошен список всех пользователей.");
        return new ArrayList<>(userRepository.getUsers().values());
    }
}
