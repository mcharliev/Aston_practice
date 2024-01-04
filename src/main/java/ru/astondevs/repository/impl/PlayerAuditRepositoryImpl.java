package ru.astondevs.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.astondevs.aop.annotations.Loggable;
import ru.astondevs.model.entity.PlayerAudit;
import ru.astondevs.repository.PlayerAuditRepository;


import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория аудита игроков.
 */
@Loggable
@Repository
public class PlayerAuditRepositoryImpl implements PlayerAuditRepository {
    private final DataSource dataSource;
    private final Logger log = LoggerFactory.getLogger(PlayerAuditRepositoryImpl.class);

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param dataSource Источник данных для работы с базой данных, тип {@link DataSource}.
     */
    @Autowired
    public PlayerAuditRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Добавляет запись аудита игрока в базу данных.
     *
     * @param playerAudit Запись аудита игрока, тип {@link PlayerAudit}.
     * @throws RuntimeException Если произошла ошибка при добавлении записи аудита.
     */
    public void addPlayerAudit(PlayerAudit playerAudit) {
        String sql = "INSERT INTO wallet_service.audit_actions (player_id, action_type, action_data, details) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, playerAudit.getPlayerId());
            preparedStatement.setString(2, playerAudit.getActionType());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(playerAudit.getActionDate()));
            preparedStatement.setString(4, playerAudit.getDetails());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Ошибка при добавлении записи аудита", e);
            throw new RuntimeException("Ошибка при добавлении записи аудита", e);
        }
    }

    /**
     * Находит записи аудита по идентификатору игрока.
     *
     * @param playerId Идентификатор игрока, тип {@link Long}.
     * @return Список записей аудита, тип {@link List} {@link PlayerAudit}.
     * @throws RuntimeException Если произошла ошибка при поиске записей аудита.
     */
    public List<PlayerAudit> findAuditsByPlayerId(Long playerId) {
        String sql = "SELECT * FROM wallet_service.audit_actions WHERE player_id = ?";

        List<PlayerAudit> audits = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, playerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    audits.add(getPlayerAudit(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при поиске записей аудита для игрока с ID: " + playerId, e);
            throw new RuntimeException("Ошибка при поиске записей аудита для игрока с ID: " + playerId, e);
        }

        return audits;
    }

    /**
     * Приватный метод для создания объекта {@link PlayerAudit} из данных результата SQL-запроса.
     *
     * @param resultSet Результат выполнения SQL-запроса, тип {@link ResultSet}.
     * @return Объект {@link PlayerAudit}, созданный из данных результата запроса.
     * @throws SQLException В случае ошибки при обработке результата SQL-запроса.
     */
    private PlayerAudit getPlayerAudit(ResultSet resultSet) throws SQLException {
        PlayerAudit audit = new PlayerAudit();
        audit.setId(resultSet.getLong("id"));
        audit.setPlayerId(resultSet.getLong("player_id"));
        audit.setActionType(resultSet.getString("action_type"));
        audit.setActionDate(resultSet.getTimestamp("action_data").toLocalDateTime());
        audit.setDetails(resultSet.getString("details"));
        return audit;
    }
}
