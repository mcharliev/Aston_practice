package ru.astondevs.model.dto;

import java.math.BigDecimal;

/**
 * Класс, представляющий баланс игрока.
 */
public class PlayerBalanceDTO {
    private String username;
    private BigDecimal balance;

    /**
     * Конструктор класса PlayerBalanceDTO.
     *
     * @param username Имя игрока.
     * @param balance  Баланс игрока, тип {@link BigDecimal}.
     */
    public PlayerBalanceDTO(String username, BigDecimal balance) {
        this.username = username;
        this.balance = balance;
    }

    /**
     * Получает имя игрока.
     *
     * @return Имя игрока.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает имя игрока.
     *
     * @param username Имя игрока.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Получает баланс игрока.
     *
     * @return Баланс игрока, тип {@link BigDecimal}.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Устанавливает баланс игрока.
     *
     * @param balance Баланс игрока, тип {@link BigDecimal}.
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}