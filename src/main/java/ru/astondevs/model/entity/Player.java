package ru.astondevs.model.entity;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Класс, представляющий сущность "игрок" с информацией о его идентификаторе, имени,
 * пароле, балансе и списке транзакций.
 */
public class Player {
    private Long id;
    private String username;
    private String password;
    private BigDecimal balance;
    private final List<Transaction> transactions = new ArrayList<>();

    /**
     * Конструктор класса Player для создания объекта игрока с указанным именем и паролем.
     *
     * @param username Имя игрока.
     * @param password Пароль игрока.
     */
    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = new BigDecimal(0);
    }

    /**
     * Пустой конструктор класса Player.
     */
    public Player() {
    }

    /**
     * Добавление транзакции к списку транзакций игрока.
     *
     * @param transaction Транзакция для добавления, см. {@link Transaction}.
     */
    public void setTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    /**
     * Получение идентификатора игрока.
     *
     * @return Идентификатор игрока.
     */
    public Long getId() {
        return id;
    }

    /**
     * Установка идентификатора игрока.
     *
     * @param id Идентификатор игрока.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получение имени игрока.
     *
     * @return Имя игрока.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Установка имени игрока.
     *
     * @param username Имя игрока.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Получение пароля игрока.
     *
     * @return Пароль игрока.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Установка пароля игрока.
     *
     * @param password Пароль игрока.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Получение баланса игрока.
     *
     * @return Баланс игрока, см. {@link BigDecimal}.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Установка баланса игрока.
     *
     * @param balance Баланс игрока, см. {@link BigDecimal}.
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Получение списка транзакций игрока.
     *
     * @return Список транзакций игрока, см. {@link Transaction}.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
