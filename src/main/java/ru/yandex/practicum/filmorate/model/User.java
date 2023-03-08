package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private long id;
    @Email
    @NotEmpty
    @NotNull(message = "Email не может быть null")
    private String email;
    @NotBlank
    @NotNull(message = "Логин не может быть null")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$",
            message = "Логин должен быть от 4 до 12 символов, состоять из английских букв разного регистра и цифр, " +
                    "без специальных символов")
    private String login;
    private String name;
    @PastOrPresent
    @NotNull(message = "Дата рождения не может быть null")
    private LocalDate birthday;
}
