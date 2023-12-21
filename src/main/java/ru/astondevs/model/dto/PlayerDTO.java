package ru.astondevs.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;


/**
 * Класс, представляющий собой объект передачи данных (DTO) для игрока.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerDTO {

    private Long id;

    private String username;

    private BigDecimal balance;

    /**
     * Получить идентификатор игрока.
     *
     * @return идентификатор игрока {@link Long}
     */
    public Long getId() {
        return id;
    }

    /**
     * Установить идентификатор игрока.
     * @param id идентификатор игрока
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить имя пользователя игрока.
     * @return имя пользователя
     */
    public String getUsername() {
        return username;
    }

    /**
     * Установить имя пользователя игрока.
     * @param username имя пользователя
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Получить баланс игрока.
     * @return баланс игрока в виде объекта {@link BigDecimal}
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Установить баланс игрока.
     * @param balance баланс игрока
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
