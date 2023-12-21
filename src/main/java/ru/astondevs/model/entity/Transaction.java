package ru.astondevs.model.entity;


import ru.astondevs.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс, представляющий сущность "транзакция" с информацией о её идентификаторе, типе,
 * сумме и времени выполнения.
 */
public class Transaction {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime localDateTime;

    /**
     * Получение идентификатора транзакции.
     *
     * @return Идентификатор транзакции.
     */
    public Long getId() {
        return id;
    }

    /**
     * Установка идентификатора транзакции.
     *
     * @param id Идентификатор транзакции.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получение типа транзакции.
     *
     * @return Тип транзакции, см. {@link TransactionType}.
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * Установка типа транзакции.
     *
     * @param type Тип транзакции, см. {@link TransactionType}.
     */
    public void setType(TransactionType type) {
        this.type = type;
    }

    /**
     * Получение суммы транзакции.
     *
     * @return Сумма транзакции, см. {@link BigDecimal}.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Установка суммы транзакции.
     *
     * @param amount Сумма транзакции, см. {@link BigDecimal}.
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Получение времени выполнения транзакции.
     *
     * @return Время выполнения транзакции, см. {@link LocalDateTime}.
     */
    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    /**
     * Установка времени выполнения транзакции.
     *
     * @param localDateTime Время выполнения транзакции, см. {@link LocalDateTime}.
     */
    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    /**
     * Переопределение метода equals для сравнения транзакций на основе их полей.
     *
     * @param o Объект, с которым сравнивается текущая транзакция.
     * @return true, если транзакции равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) && type == that.type && Objects.equals(amount, that.amount) && Objects.equals(localDateTime, that.localDateTime);
    }

    /**
     * Переопределение метода hashCode для вычисления хэш-кода транзакции.
     *
     * @return Хэш-код транзакции.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, type, amount, localDateTime);
    }
}
