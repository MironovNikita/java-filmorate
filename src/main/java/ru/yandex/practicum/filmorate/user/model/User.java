package ru.yandex.practicum.filmorate.user.model;

import lombok.*;

import java.time.LocalDate;


@With
@Value
public class User {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}
