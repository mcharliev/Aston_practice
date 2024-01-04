package ru.astondevs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.astondevs.aop.annotations.Loggable;
import ru.astondevs.model.entity.PlayerAudit;
import ru.astondevs.repository.PlayerAuditRepository;
import ru.astondevs.service.PlayerAuditService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация сервиса для работы с аудитом действий игроков.
 */
@Service
@Loggable
public class PlayerAuditServiceImpl implements PlayerAuditService {

    private final PlayerAuditRepository playerAuditRepository;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param playerAuditRepository Репозиторий аудита игроков, тип {@link PlayerAuditRepository}.
     */
    @Autowired
    public PlayerAuditServiceImpl(PlayerAuditRepository playerAuditRepository) {
        this.playerAuditRepository = playerAuditRepository;
    }

    /**
     * Логирует действие игрока.
     *
     * @param playerId   Идентификатор игрока, для которого логируется действие, тип {@link Long}.
     * @param actionType Тип действия, тип {@link String}.
     * @param details    Детали действия, тип {@link String}.
     */
    @Override
    public void logPlayerAction(Long playerId, String actionType, String details) {
        PlayerAudit audit = new PlayerAudit();
        audit.setPlayerId(playerId);
        audit.setActionType(actionType);
        audit.setDetails(details);
        audit.setActionDate(LocalDateTime.now());

        playerAuditRepository.addPlayerAudit(audit);
    }

    /**
     * Получает аудит действий для игрока.
     *
     * @param playerId Идентификатор игрока, для которого запрашивается аудит, тип {@link Long}.
     * @return Список записей аудита для игрока, тип {@link List} {@link PlayerAudit}.
     */
    @Override
    public List<PlayerAudit> getAuditForPlayer(Long playerId) {
        return playerAuditRepository.findAuditsByPlayerId(playerId);
    }
}
