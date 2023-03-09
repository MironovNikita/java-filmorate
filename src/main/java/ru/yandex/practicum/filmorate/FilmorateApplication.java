package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}
	/* Семён, привет! Спасибо, что помог разобраться с аннотациями. Это и вправду упрощает жизнь. Мой код сократился в
	два раза.
	С null-проверками тоже разобрался. Понял, что они для старой реализации.

	Теперь по порядку:
	Films:
	@Positive - понял. По ТЗ продолжительность всегда положительна должна быть, 0 недопустим - оставляем.
	По названию фильма оставил только @NotBlank - она проверяет и пустоту строки, и null
	По длине описания фильма согласен, поправил.
	По поводу даты релиза ставил @NotNull, чтобы не было ошибки в контроллере при проверке даты. Убрал тут и добавил
	проверку на null в контроллере, чтобы при get не получить NPE.

	User:
	Адрес почты: @Email - проверка на корректный адрес почты, @NotNull убрал, поставил @NotBlank
	Логин: поставил @NotBlank и @Pattern для проверки на корректность вводимых символов
	Имя: по имени согласен. Но, думаю, в ТЗ имели в виду, что пустой равносильно null или "".
	По дате рождения согласен - убрал проверку на null. @PastOrPresent как раз проверяет, что значение даты находится в
	прошлом, включая настоящее.
	*/
}
