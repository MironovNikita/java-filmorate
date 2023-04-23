package ru.yandex.practicum.filmorate.user.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.common.mapper.Mapper;
import ru.yandex.practicum.filmorate.user.model.User;

@Component
public class UserDaoTransformToUser implements Mapper<UserDao, User> {
    @Override
    public User transformFrom(UserDao userDao) {
        String name;
        if (userDao.getName() == null || userDao.getName().isBlank()) {
            name = userDao.getLogin();
        } else {
            name = userDao.getName();
        }
        return new User(userDao.getId(), userDao.getEmail(), userDao.getLogin(), name, userDao.getBirthday());
    }
}
