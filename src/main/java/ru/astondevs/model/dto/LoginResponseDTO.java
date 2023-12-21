package ru.astondevs.model.dto;

/**
 * Класс, представляющий объект ответа на успешную аутентификацию пользователя.
 *
 * Этот класс содержит информацию о зарегистрированном игроке, сгенерированном токене
 * и сообщении об успешной аутентификации.
 */
public class LoginResponseDTO {

    private PlayerDTO player;
    private String token;
    private String message;

    /**
     * Конструктор класса LoginResponseDTO для создания объекта ответа с указанным игроком и токеном.
     *
     * @param player Информация о зарегистрированном игроке, см. {@link PlayerDTO}.
     * @param token Сгенерированный токен для аутентификации.
     */
    public LoginResponseDTO(PlayerDTO player, String token) {
        this.player = player;
        this.token = token;
        this.message = String.format("Пользователь '%s' совершил успешный вход", player.getUsername());
    }

    /**
     * Пустой конструктор класса LoginResponseDTO.
     */
    public LoginResponseDTO() {
    }

    /**
     * Получение информации о зарегистрированном игроке.
     *
     * @return Объект, представляющий информацию о зарегистрированном игроке, см. {@link PlayerDTO}.
     */
    public PlayerDTO getPlayer() {
        return player;
    }

    /**
     * Установка информации о зарегистрированном игроке.
     *
     * @param player Объект, представляющий информацию о зарегистрированном игроке, см. {@link PlayerDTO}.
     */
    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

    /**
     * Получение сгенерированного токена для аутентификации.
     *
     * @return Сгенерированный токен для аутентификации.
     */
    public String getToken() {
        return token;
    }

    /**
     * Установка сгенерированного токена для аутентификации.
     *
     * @param token Сгенерированный токен для аутентификации.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Получение сообщения об успешной аутентификации.
     *
     * @return Сообщение об успешной аутентификации.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Установка сообщения об успешной аутентификации.
     *
     * @param message Сообщение об успешной аутентификации.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Получение информации о зарегистрированном игроке в виде объекта PlayerDTO.
     *
     * @return Объект, представляющий информацию о зарегистрированном игроке, см. {@link PlayerDTO}.
     */
    public PlayerDTO getPlayerDTO() {
        return player;
    }
}