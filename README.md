

<p align="center">

  <img width="160" height="160" src="https://media.tenor.com/XzYfLz7enPQAAAAi/five-star-movie-movie-review.gif">

</p>

# Filmorate

Потрясающее приложение Filmorate позволит вам составить свой собственный рейтинг фильмов и добавлять друзей, чтобы обсуждать с ними ваши любимые фильмы и обмениваться ими!

### Описание
Приложение даёт возможность составлять рейтинг для фильмов. У них присутствуют не только жанры, но и возрастной рейтинг, продолжительность, дата релиза, описание и др. Пользователи могут добавлять друг друга в друзья, смотреть список общих друзей, ставить лайки фильмам. Также реализована возможность вывести топ самых популярных фильмов по количеству лайков.

## Диаграмма базы данных

![Filmorate database scheme](https://github.com/MironovNikita/java-filmorate/blob/main/Filmorate%20database%20scheme.png?raw=true)

# Эндпоинты

### Users
- `[POST] /users` - create a new user
- `[PUT] /users` - update existing user
- `[PUT] /users/{id}/friends/{friendId}` - make two users with `id` and `friendId` as friends :)
- `[GET] /users` - get all users
- `[GET] /users/{id}` - get user by some `id`
- `[GET] /users/{id}/friends` - get all friends of user by some `id`
- `[GET] /users/{id}/friends/common/{otherId}` - get common friends of two users with `id` and `otherId`
- `[DELETE] /users/{id}` - delete user with `id`
- `[DELETE] /users/{id}/friends/{friendId}` - finish friendship of two users with `id` and `friendId` :c

### Films
- `[POST] /films` - create a new film
- `[PUT] /films` - update existing film
- `[PUT] /films/{id}/like/{userId}` - add like from user with `userId` to the film with `id` :)
- `[GET] /films` - get all films
- `[GET] /films/{id}` - get film by some `id`
- `[GET] /films/popular?count={count}` - get amount (as `count`) of the most popular films
- `[DELETE] /films/{id}` - delete film with `id`
- `[DELETE] /films/{id}/like/{userId}` - delete like from user with `userId` to the film with `id` :c


## Скриншоты
**Пример логирования работы программы**
![Логирование](https://github.com/MironovNikita/java-filmorate/blob/main/screenshots/scr1.png?raw=true)

**Тестовая таблица пользователей**
![Пользователи](https://github.com/MironovNikita/java-filmorate/blob/main/screenshots/scr4.png?raw=true)

**Тестовая таблица фильмов**
![Фильмы](https://github.com/MironovNikita/java-filmorate/blob/main/screenshots/scr2.png?raw=true)

**Тестовая таблица дружбы пользователей**
![Дружба](https://github.com/MironovNikita/java-filmorate/blob/main/screenshots/scr3.png?raw=true)
## 🚀 Обо мне
Я разрабатывал данный проект на языке Java 11 в рамках курса Яндекс.Практикум "Java-Разработчик". Данный проект мне понравился, достаточно приближен к реальному, а также позволил потренироваться в работе с базами данных.




## 🛠 Применяемые технологии
- `Spring Boot`
- `Maven`
- `Lombok`


## Автор

- [@MironovNikita](https://github.com/MironovNikita)

