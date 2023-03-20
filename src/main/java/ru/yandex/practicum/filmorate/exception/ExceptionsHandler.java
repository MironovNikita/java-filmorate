package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceptionsHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIncorrectDate(DateValidationException exception) {
        log.warn("400 - Некорректная дата обрабатываемого объекта", exception);
        return Map.of(exception.getDescription(), exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIncorrectPathData(IncorrectPathDataException exception) {
        log.error("400 - Обнаружены некорректные данные в запросе", exception);
        return Map.of(exception.getDescription(), exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleObjectNotFound(ObjectNotFoundException exception) {
        log.error("404 - Искомый объект не найден", exception);
        return Map.of("Искомый объект не найден", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalErrors(Exception exception) {
        log.error("500 - Внутренняя ошибка сервера", exception);
        return Map.of("Внутрення ошибка сервера", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleEmptyObject(EmptyObjectException exception) {
        log.error("404 - Передаваемый объект пуст", exception);
        return Map.of("Переданный объект пуст", exception.getMessage());
    }
}
