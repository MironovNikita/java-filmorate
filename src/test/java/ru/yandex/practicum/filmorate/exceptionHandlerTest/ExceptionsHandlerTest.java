package ru.yandex.practicum.filmorate.exceptionHandlerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.common.exception.DateValidationException;
import ru.yandex.practicum.filmorate.common.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.film.dao.FilmDao;
import ru.yandex.practicum.filmorate.film.service.FilmService;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
public class ExceptionsHandlerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    FilmService filmService;

    private FilmDao createFilmDao() {
        return new FilmDao(
                1L,
                "Fast and Furious",
                "The Fast and the Furious",
                LocalDate.of(2000, 6, 18),
                106,
                new Mpa(3, "PG-13"),
                Collections.emptyList()
        );
    }

    @DisplayName("Проверка исключения DateValidation")
    @Test
    void dateValidationExceptionTest() throws Exception {
        FilmDao filmDao = createFilmDao().withReleaseDate(LocalDate.of(1000, 1, 1));
        String json = objectMapper.writeValueAsString(filmDao);

        DateValidationException exception = new DateValidationException("Дата выпуска фильма не должна быть до 1895-12-28");

        Map<String, String> dateValidError = Map.of("Ошибка валидации даты", exception.getMessage());

        mockMvc.perform(post("/films").contentType("application/json").content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(dateValidError)));
    }

    @DisplayName("Проверка исключения ObjectNotFound")
    @Test
    void objectNotFoundExceptionTest() throws Exception {
        Long id = 1L;

        Map<String, String> notFound = Map.of("Искомый объект не найден", "Фильм с id: " + id + " не найден!");
        when(filmService.getById(id)).thenThrow(new ObjectNotFoundException("Фильм", id));

        mockMvc.perform(get("/films/" + id).contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(notFound)));
    }

    @DisplayName("Проверка исключения InternalServerError")
    @Test
    void internalServerErrorExceptionTest() throws Exception {
        mockMvc.perform(get("/films/nnn").contentType("application/json"))
                .andExpect(status().is(500));
    }
}
