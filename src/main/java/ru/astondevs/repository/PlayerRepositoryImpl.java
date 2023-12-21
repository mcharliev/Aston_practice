package ru.astondevs.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.astondevs.aop.annotations.Loggable;
import ru.astondevs.connection.DatabaseConnectionManager;
import ru.astondevs.model.entity.Player;

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
public class PlayerRepositoryImpl implements PlayerRepository {
    public PlayerRepositoryImpl(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    private final DatabaseConnectionManager connectionManager;
    private final Logger log = LoggerFactory.getLogger(PlayerRepositoryImpl.class);

    /**
     * Добавляет нового игрока в базу данных.
     *
     * @param player Объект игрока, который следует добавить, см. {@link Player}.
     * @throws RuntimeException если произошла ошибка при добавлении игрока.
     */
    public void addPlayer(Player player) {
        // SQL-запрос для вставки нового игрока в таблицу.
        String sql = "INSERT INTO wallet_service.players (id, username, password, balance) VALUES (nextval('wallet_service.players_seq'), ?, ?, ?) RETURNING id";

        try (Connection connection = connectionManager.getConnection();
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
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка при добавлении игрока", e);
        }
    }

    /**
     * Ищет игрока в базе данных по его ID.
     *
     * @param id Идентификатор игрока.
     * @return {@link Optional} объект игрока, если он найден, иначе пустой {@link Optional}.
     * @throws RuntimeException если произошла ошибка при поиске игрока по ID.
     */
    public Optional<Player> findPlayerById(Long id) {
        // SQL-запрос для поиска игрока по ID.
        String sql = "SELECT * FROM wallet_service.players WHERE id = ?";

        try (Connection connection = connectionManager.getConnection();
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
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Ошибка при поиске игрока id: " + id, e);
            throw new RuntimeException("Ошибка при поиске игрока id: " + id, e);
        }
    }

    /**
     * Ищет игрока в базе данных по его имени пользователя.
     *
     * @param username Имя игрока.
     * @return {@link Optional} объект игрока, если он найден, иначе пустой {@link Optional}.
     * @throws RuntimeException если произошла ошибка при поиске игрока по имени пользователя.
     */
    public Optional<Player> findPlayerByUsername(String username) {
        // SQL-запрос для поиска игрока по имени пользователя.
        String sql = "SELECT * FROM wallet_service.players WHERE username = ?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Устанавливаем имя пользователя как параметр для запроса.
            preparedStatement.setString(1, username);

            // Выполнение запроса и проверка, существует ли такой игрок.
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(getPlayer(resultSet));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Ошибка при поиске игрока по имени: " + username, e);
            throw new RuntimeException("Ошибка при поиске игрока по имени: " + username, e);
        }

        return Optional.empty(); // Если игрок не найден, возвращаем пустой Optional.
    }

    /**
     * Обновляет информацию о балансе игрока в базе данных.
     *
     * @param player Объект игрока с новой информацией о балансе, см. {@link Player}.
     * @throws RuntimeException если произошла ошибка при обновлении информации об игроке.
     */
    public void updatePlayer(Player player) {
        // SQL-запрос для обновления баланса игрока.
        String sql = "UPDATE wallet_service.players SET balance = ? WHERE id = ?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Устанавливаем новый баланс и ID игрока для обновления.
            preparedStatement.setBigDecimal(1, player.getBalance());
            preparedStatement.setLong(2, player.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            log.error("Ошибка при обновлении игрока: " + player.getUsername(), e);
            throw new RuntimeException("Ошибка при обновлении игрока: " + player.getUsername(), e);
        }
    }

    /**
     * Преобразует результат SQL-запроса (строку из {@link ResultSet}) в объект {@link Player}.
     *
     * @param resultSet Результат SQL-запроса.
     * @return {@link Player} объект игрока, извлеченный из текущей строки {@link ResultSet}.
     * @throws SQLException в случае ошибки при извлечении данных из {@link ResultSet}.
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
