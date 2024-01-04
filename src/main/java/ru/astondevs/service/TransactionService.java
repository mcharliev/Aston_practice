package ru.astondevs.service;

import ru.astondevs.model.dto.AmountDTO;
import ru.astondevs.model.dto.TransactionDTO;
import ru.astondevs.model.dto.TransactionHistoryDTO;
import ru.astondevs.model.entity.Player;


/**
 * Интерфейс для работы с транзакциями игроков.
 */
public interface TransactionService {

    /**
     * Добавляет дебетовую транзакцию для игрока.
     *
     * @param player    Игрок, для которого создается транзакция, тип {@link Player}.
     * @param amountDTO Информация о сумме транзакции, тип {@link AmountDTO}.
     * @return Детали созданной дебетовой транзакции, тип {@link TransactionDTO}.
     */
    TransactionDTO addDebitTransaction(Player player, AmountDTO amountDTO);

    /**
     * Добавляет кредитовую транзакцию для игрока.
     *
     * @param player    Игрок, для которого создается транзакция, тип {@link Player}.
     * @param amountDTO Информация о сумме транзакции, тип {@link AmountDTO}.
     * @return Детали созданной кредитовой транзакции, тип {@link TransactionDTO}.
     */
    TransactionDTO addCreditTransaction(Player player, AmountDTO amountDTO);

    /**
     * Просматривает историю транзакций для игрока.
     *
     * @param player Игрок, для которого просматривается история транзакций, тип {@link Player}.
     * @return История транзакций игрока, тип {@link TransactionHistoryDTO}.
     */
    TransactionHistoryDTO viewTransactionHistory(Player player);
}
