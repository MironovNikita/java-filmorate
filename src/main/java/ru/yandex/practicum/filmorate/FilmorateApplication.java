package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}
	/* Семён, привет! По традиции пишу в main. Вроде приложение готово. Spring - вещь!
	Возникло пару вопросов, пока писал ТЗ:
	1) Зачем делать return в таких методах как create/update?
	2) Когда пишу тесты, в аннотации @DisplayName пишу название на кириллице. Но у меня в IDEA почему-то вместо
	кириллицы отображаются ������������ � ������ �������. Я поискал в интернете информацию. Вроде как
	это ошибка внутри то ли Maven, то ли JUnit. Так и не смог найти причину. При чём в предыдущей работе таких проблем
	не было. И кириллица нормально отображается везде, кроме окна с тестами. Не знаешь, в чём причина может быть?
	*/
}
