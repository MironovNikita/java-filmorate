package ru.yandex.practicum.filmorate.film.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.common.mapper.Mapper;
import ru.yandex.practicum.filmorate.film.model.Film;

import java.util.LinkedHashSet;
import java.util.Optional;

@Component
public class FilmDaoTransformToFilm implements Mapper<FilmDao, Film> {
    @Override
    public Film transformFrom(FilmDao filmDao) {
        return new Film(filmDao.getId(), filmDao.getName(), filmDao.getDescription(), filmDao.getReleaseDate(),
                filmDao.getDuration(), filmDao.getMpa(),
                Optional.ofNullable(filmDao.getGenres()).orElse(new LinkedHashSet<>()));
    }
}
