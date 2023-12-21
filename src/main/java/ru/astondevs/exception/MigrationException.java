package ru.astondevs.exception;

/**
 * Исключение, сигнализирующее о проблемах в процессе миграции данных.
 * Это общее исключение для всех ситуаций, когда возникают ошибки при миграции.
 */
public class MigrationException extends RuntimeException{
    public MigrationException(String message) {
        super(message);
    }
}
