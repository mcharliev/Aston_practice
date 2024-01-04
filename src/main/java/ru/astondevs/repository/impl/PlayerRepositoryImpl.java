package ru.astondevs.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.astondevs.aop.annotations.Loggable;
import ru.astondevs.model.entity.Player;
import ru.astondevs.repository.PlayerRepository;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


/**
 * Репозиторий для работы с объектами типа {@link Player} в базе данных.
 * Предоставляет функции для добавления, поиска и обновления информации об игроках.
 */
@Loggable
@Repository
public class PlayerRepositoryImpl implements PlayerRepository {
    private final DataSource dataSource;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param dataSource Источник данных для работы с базой данных, тип {@link DataSource}.
     */
    @Autowired
    public PlayerRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final Logger log = LoggerFactory.getLogger(PlayerRepositoryImpl.class);

    /**
     * Добавляет игрока в базу данных.
     *
     * @param player Игрок для добавления, тип {@link Player}.
     * @throws RuntimeException Если произошла ошибка при добавлении игрока.
     */
    public void addPlayer(Player player) {
        // SQL-запрос для вставки нового игрока в таблицу.
        String sql = "INSERT INTO wallet_service.players (id, username, password, balance) VALUES (nextval('wallet_service.players_seq'), ?, ?, ?) RETURNING id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Установка значений для параметров SQL-запроса.
            preparedStatement.setString(1, player.getUsername());
            preparedStatement.setString(2, player.getPassword());
            preparedStatement.setBigDecimal(3, player.getBalance());

            // Выполнение запроса и обработка результата.
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Устанавливаем ID, сгенерированный базой данных, для объекта игрока.
                    long generatedId = resultSet.getLong(1);
                    player.setId(generatedId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении игрока", e);
        }
    }

    /**
     * Находит игрока по его идентификатору.
     *
     * @param id Идентификатор игрока, тип {@link Long}.
     * @return Объект игрока в виде {@link Optional<Player>}.
     * @throws RuntimeException Если произошла ошибка при поиске игрока.
     */
    public Optional<Player> findPlayerById(Long id) {
        // SQL-запрос для поиска игрока по ID.
        String sql = "SELECT * FROM wallet_service.players WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Установка ID игрока как параметра для запроса.
            preparedStatement.setLong(1, id);

            // Выполнение запроса и проверка, существует ли такой игрок.
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(getPlayer(resultSet));
                } else {
                    return Optional.empty(); // Если игрок не найден, возвращаем пустой Optional.
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при поиске игрока id: " + id);
            throw new RuntimeException("Ошибка при поиске игрока id: " + id);
        }
    }

    /**
     * Находит игрока по его имени пользователя.
     *
     * @param username Имя пользователя игрока, тип {@link String}.
     * @return Объект игрока в виде {@link Optional<Player>}.
     * @throws RuntimeException Если произошла ошибка при поиске игрока.
     */
    public Optional<Player> findPlayerByUsername(String username) {
        // SQL-запрос для поиска игрока по имени пользователя.
        String sql = "SELECT * FROM wallet_service.players WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Устанавливаем имя пользователя как параметр для запроса.
            preparedStatement.setString(1, username);

            // Выполнение запроса и проверка, существует ли такой игрок.
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(getPlayer(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при поиске игрока по имени: " + username, e);
            throw new RuntimeException("Ошибка при поиске игрока по имени: " + username, e);
        }

        return Optional.empty(); // Если игрок не найден, возвращаем пустой Optional.
    }

    /**
     * Обновляет баланс игрока в базе данных.
     *
     * @param player Игрок для обновления, тип {@link Player}.
     * @throws RuntimeException Если произошла ошибка при обновлении игрока.
     */
    public void updatePlayer(Player player) {
        // SQL-запрос для обновления баланса игрока.
        String sql = "UPDATE wallet_service.players SET balance = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Устанавливаем новый баланс и ID игрока для обновления.
            preparedStatement.setBigDecimal(1, player.getBalance());
            preparedStatement.setLong(2, player.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Ошибка при обновлении игрока: " + player.getUsername(), e);
            throw new RuntimeException("Ошибка при обновлении игрока: " + player.getUsername(), e);
        }
    }

    /**
     * Приватный метод для создания объекта {@link Player} из данных результата SQL-запроса.
     *
     * @param resultSet Результат выполнения SQL-запроса, тип {@link ResultSet}.
     * @return Объект {@link Player}, созданный из данных результата запроса.
     * @throws SQLException В случае ошибки при обработке результата SQL-запроса.
     */
    private Player getPlayer(ResultSet resultSet) throws SQLException {
        Player player = new Player();
        player.setId(resultSet.getLong("id"));
        player.setUsername(resultSet.getString("username"));
        player.setPassword(resultSet.getString("password"));
        player.setBalance(resultSet.getBigDecimal("balance"));
        return player;
    }
}
