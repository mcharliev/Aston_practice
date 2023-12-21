package ru.astondevs.repository;

import ru.astondevs.model.entity.Player;

import java.util.Optional;

/**
 * Интерфейс, представляющий репозиторий для работы с данными об игроках в базе данных.
 */
public interface PlayerRepository {
    /**
     * Добавляет нового игрока в базу данных.
     *
     * @param player Объект игрока, который следует добавить, см. {@link Player}.
     */
    void addPlayer(Player player);

    /**
     * Ищет игрока в базе данных по его ID.
     *
     * @param id Идентификатор игрока.
     * @return {@link Optional} объект игрока, если он найден, иначе пустой {@link Optional}.
     */
    Optional<Player> findPlayerById(Long id);

    /**
     * Ищет игрока в базе данных по его имени пользователя.
     *
     * @param username Имя игрока.
     * @return {@link Optional} объект игрока, если он найден, иначе пустой {@link Optional}.
     */
    Optional<Player> findPlayerByUsername(String username);

    /**
     * Обновляет информацию о балансе игрока в базе данных.
     *
     * @param player Объект игрока с новой информацией о балансе, см. {@link Player}.
     */
    void updatePlayer(Player player);
}

