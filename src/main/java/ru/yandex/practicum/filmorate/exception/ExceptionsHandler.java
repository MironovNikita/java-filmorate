package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceptionsHandler {
    @ExceptionHandler
    public Map<String, String> handleIncorrectDate(DateValidationException exception) {
        log.warn("Выброшено исключение проверки даты", exception);
        return Map.of(exception.getDescription(), exception.getMessage());
    }

    @ExceptionHandler
    public Map<String, String> handleIncorrectPathData(IncorrectPathDataException exception) {
        log.error("Выброшено исключение проверки корректности данных в запросе", exception);
        return Map.of(exception.getDescription(), exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleObjectNotFound(ObjectNotFoundException exception) {
        log.error("Выброшено исключение ненайденного объекта", exception);
        return Map.of("Объект не найден", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalErrors(Exception exception) {
        log.error("Внутренняя ошибка сервера", exception);
        return Map.of("Внутрення ошибка сервера", exception.getMessage());
    }
}
