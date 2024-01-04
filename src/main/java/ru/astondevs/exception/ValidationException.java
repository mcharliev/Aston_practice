package ru.astondevs.exception;


import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Исключение, выбрасываемое при нарушении  валидации
 */
public class ValidationException extends RuntimeException {
    private final Set<? extends ConstraintViolation<?>> violations;

    /**
     * Конструктор класса `ValidationException`.
     *
     * @param message    Сообщение об ошибке.
     * @param violations Набор нарушений валидации.
     */
    public ValidationException(String message, Set<? extends ConstraintViolation<?>> violations) {
        super(message);
        this.violations = violations;
    }

    /**
     * Получает набор нарушений валидации.
     *
     * @return Набор нарушений валидации.
     */
    public Set<? extends ConstraintViolation<?>> getViolations() {
        return violations;
    }
}