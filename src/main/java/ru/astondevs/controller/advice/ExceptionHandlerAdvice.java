package ru.astondevs.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.astondevs.exception.*;


/**
 * Обработчик исключений для контроллеров, помеченных аннотацией {@code @RestController}.
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * Обрабатывает исключение {@link PlayerAlreadyExistException}.
     *
     * @return Ответ с сообщением об ошибке и статусом конфликта (409).
     */
    @ExceptionHandler(PlayerAlreadyExistException.class)
    public ResponseEntity<ExceptionDetails> handlePlayerAlreadyExistException() {
        ExceptionDetails exceptionDetails = new ExceptionDetails();
        exceptionDetails.setMessage("Игрок с таким именем уже существует");
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exceptionDetails);
    }

    /**
     * Обрабатывает исключение {@link AuthenticationException}.
     *
     * @return Ответ с сообщением об ошибке и статусом "Несанкционированный доступ" (401).
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionDetails> handleAuthenticationException() {
        ExceptionDetails exceptionDetails = new ExceptionDetails();
        exceptionDetails.setMessage("Ошибка авторизации, пожалуйста проверьте введенные данные");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exceptionDetails);
    }

    /**
     * Обрабатывает исключение {@link JwtException}.
     *
     * @return Ответ с сообщением об ошибке и статусом "Несанкционированный доступ" (401).
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ExceptionDetails> handleJwtException() {
        ExceptionDetails exceptionDetails = new ExceptionDetails();
        exceptionDetails.setMessage("Ошибка авторизации, токен отсутствует или недействителен");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exceptionDetails);
    }

    /**
     * Обрабатывает исключение {@link NotEnoughMoneyException}.
     *
     * @return Ответ с сообщением об ошибке и статусом конфликта (409).
     */
    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<ExceptionDetails> handleNotEnoughMoneyException() {
        ExceptionDetails exceptionDetails = new ExceptionDetails();
        exceptionDetails.setMessage("Ошибка дебетовой транзакции, недостаточно денег на счете");
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exceptionDetails);
    }

    /**
     * Обрабатывает исключение {@link NoTransactionsFoundException}.
     *
     * @return Ответ с сообщением об ошибке и статусом "Не найдено" (404).
     */
    @ExceptionHandler(NoTransactionsFoundException.class)
    public ResponseEntity<ExceptionDetails> handleNoTransactionsFoundException() {
        ExceptionDetails exceptionDetails = new ExceptionDetails();
        exceptionDetails.setMessage("У игрока еще нету истории транзакций");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exceptionDetails);
    }

    /**
     * Обрабатывает исключение {@link ValidationException}.
     *
     * @param e Исключение {@code ValidationException}.
     * @return Ответ с сообщением об ошибке и статусом "Неверный запрос" (400).
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
