package ru.astondevs.exception;

/**
 * Исключение, которое выбрасывается в случае возникновения проблем при аутентификации.
 * <p>
 * Это подкласс класса {@link RuntimeException}, используемый для обозначения исключительных ситуаций,
 * связанных с аутентификацией пользователей или системой.
 *
 * @see RuntimeException
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
