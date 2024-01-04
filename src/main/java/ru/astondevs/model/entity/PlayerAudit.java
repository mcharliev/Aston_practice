package ru.astondevs.model.entity;


import java.time.LocalDateTime;

/**
 * Класс, представляющий аудит действий игрока.
 */
public class PlayerAudit {
    private Long id;
    private Long playerId;
    private String actionType;
    private LocalDateTime actionDate;
    private String details;

    /**
     * Получает идентификатор аудита.
     *
     * @return Идентификатор аудита, тип {@link Long}.
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор аудита.
     *
     * @param id Идентификатор аудита, тип {@link Long}.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получает идентификатор игрока.
     *
     * @return Идентификатор игрока, тип {@link Long}.
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Устанавливает идентификатор игрока.
     *
     * @param playerId Идентификатор игрока, тип {@link Long}.
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Получает тип действия игрока.
     *
     * @return Тип действия, тип {@link String}.
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * Устанавливает тип действия игрока.
     *
     * @param actionType Тип действия, тип {@link String}.
     */
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    /**
     * Получает дату и время действия игрока.
     *
     * @return Дата и время действия, тип {@link LocalDateTime}.
     */
    public LocalDateTime getActionDate() {
        return actionDate;
    }

    /**
     * Устанавливает дату и время действия игрока.
     *
     * @param actionDate Дата и время действия, тип {@link LocalDateTime}.
     */
    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }

    /**
     * Получает детали действия игрока.
     *
     * @return Детали действия, тип {@link String}.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Устанавливает детали действия игрока.
     *
     * @param details Детали действия, тип {@link String}.
     */
    public void setDetails(String details) {
        this.details = details;
    }
}
