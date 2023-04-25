package ru.yandex.practicum.filmorate.common.mapper;

public interface Mapper<R, T> {
    T transformFrom(R someObject);
}
