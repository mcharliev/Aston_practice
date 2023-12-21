package ru.astondevs.exception;

/**
 * Исключение, сигнализирующее о проблемах при валидации данных.
 * <p>
 * Это подкласс класса {@link RuntimeException}, используемый для обозначения исключительных ситуаций,
 * связанных с ошибками валидации данных.
 *
 * @see RuntimeException
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}