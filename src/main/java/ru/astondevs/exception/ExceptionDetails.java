package ru.astondevs.exception;

/**
 * Класс для представления деталей исключения.
 */
public class ExceptionDetails {
    private String message;

    /**
     * Получает сообщение об ошибке.
     *
     * @return Сообщение об ошибке.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Устанавливает сообщение об ошибке.
     *
     * @param message Сообщение об ошибке.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
