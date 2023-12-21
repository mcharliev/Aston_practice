package ru.astondevs.repository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.astondevs.aop.annotations.Loggable;
import ru.astondevs.connection.DatabaseConnectionManager;
import ru.astondevs.model.entity.Transaction;
import ru.astondevs.model.enums.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с транзакциями в базе данных.
 * Предоставляет функции для добавления транзакций и получения транзакций для определенного игрока.
 */
@Loggable
public class TransactionRepositoryImpl implements TransactionRepository {

    private final DatabaseConnectionManager connectionManager;
    private final Logger log = LoggerFactory.getLogger(TransactionRepositoryImpl.class);

    public TransactionRepositoryImpl(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Добавляет новую транзакцию в базу данных.
     *
     * @param transaction объект транзакции для добавления
     * @param playerId идентификатор игрока, для которого добавляется транзакция
     */
    public Transaction addTransaction(Transaction transaction, Long playerId) {

        // SQL-запрос для вставки новой транзакции в таблицу транзакций.
        String sql = "INSERT INTO wallet_service.transactions (id, type, amount, local_date_time, player_id) VALUES (nextval('wallet_service.transactions_seq'), ?, ?, ?, ?) RETURNING id";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Устанавливаем значения для параметров в SQL-запросе.
            preparedStatement.setString(1, transaction.getType().name());
            preparedStatement.setBigDecimal(2, transaction.getAmount());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(transaction.getLocalDateTime()));
            preparedStatement.setLong(4, playerId);

            // Выполняем запрос и обрабатываем результат.
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Присваиваем объекту транзакции ID, который был сгенерирован базой данных.
                    long generatedId = resultSet.getLong(1);
                    transaction.setId(generatedId);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Ошибка при добавлении транзакции", e);
            throw new RuntimeException("Ошибка при добавлении транзакции", e);
        }
        return transaction;
    }

    /**
     * Получает все транзакции, связанные с определенным игроком, из базы данных.
     *
     * @param playerId идентификатор игрока, для которого требуется получить транзакции
     * @return список транзакций {@link Transaction} для заданного игрока
     */
    public List<Transaction> getAllTransactionsByPlayerId(Long playerId) {
        // SQL-запрос для выборки всех транзакций определенного игрока.
        String sql = "SELECT * FROM wallet_service.transactions WHERE player_id = ?";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Устанавливаем идентификатор игрока как параметр для запроса.
            preparedStatement.setLong(1, playerId);

            // Выполняем запрос и обрабатываем результат.
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = new Transaction();
                    // Извлекаем данные из текущей строки результата и заполняем объект транзакции.
                    transaction.setId(resultSet.getLong("id"));
                    transaction.setType(TransactionType.valueOf(resultSet.getString("type")));
                    transaction.setAmount(resultSet.getBigDecimal("amount"));
                    transaction.setLocalDateTime(resultSet.getTimestamp("local_date_time").toLocalDateTime());
                    transactions.add(transaction);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Ошибка при получении всех транзакций для playerId: " + playerId, e);
            throw new RuntimeException("Ошибка при получении всех транзакций для playerId: " + playerId, e);
        }

        // Возвращаем список всех найденных транзакций для указанного игрока.
        return transactions;
    }
}