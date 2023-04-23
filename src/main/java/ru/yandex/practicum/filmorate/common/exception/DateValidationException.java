package ru.yandex.practicum.filmorate.common.exception;

public class DateValidationException extends RuntimeException {
    public DateValidationException(String message) {
        super(message);
    }
}
