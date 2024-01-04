package ru.astondevs.service;



import ru.astondevs.model.dto.LoginResponseDTO;
import ru.astondevs.model.dto.PlayerBalanceDTO;
import ru.astondevs.model.dto.PlayerDTO;
import ru.astondevs.model.dto.RegisterPlayerDTO;
import ru.astondevs.model.entity.Player;

import java.util.Optional;

/**
 * Интерфейс сервиса для работы с сущностями игрока {@link Player}.
 */
public interface PlayerService {

    /**
     * Найти игрока по его ID.
     *
     * @param id идентификатор игрока, тип {@link Long}.
     * @return найденный игрок, тип {@link Player}.
     */
    Player findPlayerById(Long id);

    /**
     * Обновить данные игрока.
     *
     * @param updatedPlayer обновленные данные игрока, тип {@link Player}.
     */
    void updatePlayer(Player updatedPlayer);

    /**
     * Зарегистрировать нового игрока.
     *
     * @param registerPlayerDTO данные для регистрации, тип {@link RegisterPlayerDTO}.
     * @return DTO нового игрока, тип {@link PlayerDTO}.
     */
    PlayerDTO registerNewPlayer(RegisterPlayerDTO registerPlayerDTO);

    /**
     * Найти игрока по имени пользователя.
     *
     * @param username имя пользователя, тип {@link String}.
     * @return найденный игрок, тип  {@link Optional<Player>}
     */
    Optional<Player> findPlayerByUsername(String username);

    /**
     * Получить информацию о балансе игрока.
     *
     * @param player игрок, тип {@link Player}.
     * @return информация о балансе в формате JSON, тип {@link PlayerBalanceDTO}.
     */
    PlayerBalanceDTO getPlayerBalanceInfo(Player player);

    /**
     * Аутентифицировать пользователя и сгенерировать токен для доступа.
     *
     * @param registerPlayerDTO данные для аутентификации, тип {@link RegisterPlayerDTO}.
     * @return DTO с данными игрока и сгенерированным токеном, тип {@link LoginResponseDTO}.
     */
    LoginResponseDTO authenticateAndGenerateToken(RegisterPlayerDTO registerPlayerDTO);

    /**
     * Проверить токен и получить игрока по нему.
     *
     * @param token токен, тип {@link String}.
     * @return игрок, связанный с токеном, тип {@link Optional<Player>}
     */
    Optional<Player> validateTokenAndGetPlayer(String token);
}