package ru.astondevs.repository;


import ru.astondevs.model.entity.Transaction;

import java.util.List;

/**
 * Интерфейс для работы с транзакциями в базе данных.
 * Предоставляет методы для добавления и получения транзакций.
 */
public interface TransactionRepository {

    /**
     * Добавляет новую транзакцию в базу данных.
     *
     * @param transaction Объект транзакции для добавления, см. {@link Transaction}.
     * @param playerId    Идентификатор игрока, для которого добавляется транзакция.
     */
    void addTransaction(Transaction transaction, Long playerId);

    /**
     * Получает все транзакции, связанные с определенным игроком, из базы данных.
     *
     * @param playerId Идентификатор игрока, для которого требуется получить транзакции.
     * @return Список транзакций {@link Transaction} для заданного игрока.
     */
    List<Transaction> getAllTransactionsByPlayerId(Long playerId);
}
