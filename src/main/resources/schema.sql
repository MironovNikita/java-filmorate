/*
 * Имена таблиц users и likes приняты во множественном числе вследствие того, что слова user и like зарезервированы
 */
CREATE TABLE IF NOT EXISTS users
(
    user_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(50) UNIQUE NOT NULL,
    login    VARCHAR(30) UNIQUE NOT NULL,
    name     VARCHAR(30)        NOT NULL,
    birthday DATE               NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS user_email_uindex ON users (email);

CREATE UNIQUE INDEX IF NOT EXISTS user_login_uindex ON users (login);

CREATE TABLE IF NOT EXISTS mpa
(
    rating_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(10) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS genre
(
    genre_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS film
(
    film_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  VARCHAR(200) NOT NULL,
    release_date DATE         NOT NULL,
    duration     INTEGER      NOT NULL,
    rating_id    INTEGER      NOT NULL,
    FOREIGN KEY (rating_id) REFERENCES mpa (rating_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  BIGINT  NOT NULL,
    genre_id INTEGER NOT NULL,
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genre (genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes
(
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendship
(
    user_id   BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (user_id) ON DELETE CASCADE
);