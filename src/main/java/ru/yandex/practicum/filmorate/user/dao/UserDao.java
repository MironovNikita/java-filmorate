package ru.yandex.practicum.filmorate.user.dao;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@EqualsAndHashCode
@With
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDao {
    @NotNull
    @PositiveOrZero
    private Long id = 0L;
    @Email
    @NotBlank(message = "Email не может быть null")
    private String email;
    @NotBlank(message = "Логин не может быть null")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$",
            message = "Логин должен быть от 4 до 12 символов, состоять из английских букв разного регистра и цифр, " +
                    "без специальных символов")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
}
