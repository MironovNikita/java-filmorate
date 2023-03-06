package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {
    private long id;
    @NonNull private String email;
    @NonNull private String login;
    private String name;
    @NonNull private LocalDate birthday;
}
