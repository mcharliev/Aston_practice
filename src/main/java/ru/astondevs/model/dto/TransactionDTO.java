package ru.astondevs.model.dto;


import ru.astondevs.model.enums.TransactionType;

import java.math.BigDecimal;


/**
 * Класс, представляющий собой объект передачи данных (DTO) для транзакции.
 */
public class TransactionDTO {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private String localDateTime;

    /**
     * Получить уникальный идентификатор транзакции.
     *
     * @return идентификатор транзакции {@link Long}
     */
    public Long getId() {
        return id;
    }

    /**
     * Установить уникальный идентификатор транзакции.
     *
     * @param id идентификатор транзакции
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить тип транзакции.
     *
     * @return тип транзакции {@link TransactionType}
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * Установить тип транзакции.
     *
     * @param type тип транзакции
     */
    public void setType(TransactionType type) {
        this.type = type;
    }

    /**
     * Получить сумму транзакции.
     *
     * @return сумма транзакции {@link BigDecimal}
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Установить сумму транзакции.
     *
     * @param amount сумма транзакции
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Получить время проведения транзакции.
     *
     * @return время проведения транзакции {@link String}
     */
    public String getLocalDateTime() {
        return localDateTime;
    }

    /**
     * Установить время проведения транзакции.
     *
     * @param localDateTime время проведения транзакции в формате строки
     */
    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }
}