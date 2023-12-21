package ru.astondevs.service;

import ru.astondevs.model.entity.Player;
import ru.astondevs.model.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * Интерфейс для работы с транзакциями игроков.
 */
public interface TransactionService {

    /**
     * Создает дебетовую транзакцию для игрока.
     *
     * @param player       игрок, для которого создается транзакция
     * @param debitAmount  сумма дебетовой транзакции
     * @return созданная дебетовая транзакция {@link Transaction}
     */
    Transaction addDebitTransaction(Player player, BigDecimal debitAmount);

    /**
     * Создает кредитовую транзакцию для игрока.
     *
     * @param player        игрок, для которого создается транзакция
     * @param creditAmount  сумма кредитовой транзакции
     * @return созданная кредитовая транзакция {@link Transaction}
     */
    Transaction addCreditTransaction(Player player, BigDecimal creditAmount);

    /**
     * Отображает историю транзакций игрока.
     *
     * @param id        идентификатор игрока
     * @param username  имя игрока
     * @return список транзакций игрока {@link List<Transaction>}
     */
    List<Transaction> viewTransactionHistory(Long id, String username);

}
