package ru.astondevs.service;



import ru.astondevs.model.entity.PlayerAudit;

import java.util.List;

/**
 * Интерфейс для работы с аудитом действий игроков.
 */
public interface PlayerAuditService {

    /**
     * Логирует действие игрока.
     *
     * @param playerId   Идентификатор игрока, для которого логируется действие, тип {@link Long}.
     * @param actionType Тип действия, тип {@link String}.
     * @param details    Детали действия, тип {@link String}.
     */
    void logPlayerAction(Long playerId, String actionType, String details);

    /**
     * Получает аудит действий для игрока.
     *
     * @param playerId Идентификатор игрока, для которого запрашивается аудит, тип {@link Long}.
     * @return Список записей аудита для игрока, тип {@link List} {@link PlayerAudit}.
     */
    List<PlayerAudit> getAuditForPlayer(Long playerId);
}
