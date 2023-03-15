package ru.yandex.practicum.filmorate.storage;

import java.util.List;

//Объединяет в себе как FilmStorage, так и UserStorage, т.к. по сути логика методов у них одинаковая
public interface Storage<T> {
    List<T> getAll();
    T getById(long id);
    T create(T object);
    T update(T object);
    T delete(long id);
}
