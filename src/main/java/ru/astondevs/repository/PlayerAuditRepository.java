package ru.astondevs.repository;

import ru.astondevs.model.entity.PlayerAudit;

import java.util.List;

/**
 * Интерфейс репозитория для работы с аудитом игроков.
 */
public interface PlayerAuditRepository {

    /**
     * Добавляет запись аудита игрока.
     *
     * @param playerAudit Запись аудита игрока, тип {@link PlayerAudit}.
     */
    void addPlayerAudit(PlayerAudit playerAudit);

    /**
     * Находит записи аудита по идентификатору игрока.
     *
     * @param playerId Идентификатор игрока, тип {@link Long}.
     * @return Список записей аудита, тип {@link List} {@link PlayerAudit}.
     */
    List<PlayerAudit> findAuditsByPlayerId(Long playerId);
}
