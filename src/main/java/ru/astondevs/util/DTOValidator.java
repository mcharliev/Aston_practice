package ru.astondevs.util;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Интерфейс для валидации объектов DTO (Data Transfer Object).
 * Предоставляет метод для валидации объекта DTO и возвращает набор нарушений ограничений валидации.
 *
 * @param <T> тип объекта DTO, подлежащего валидации.
 */
public interface DTOValidator<T> {

    /**
     * Выполняет валидацию объекта DTO.
     *
     * @param dto объект, который нужно валидировать, тип {@code T}.
     * @return набор нарушений ограничений валидации
     */
    Set<ConstraintViolation<T>> validate(T dto);
}
