package ru.astondevs.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



/**
 * Класс, представляющий собой объект передачи данных (DTO) для регистрации игрока.
 */
public class RegisterPlayerDTO {

    @NotNull(message = "Имя пользователя обязательно для заполнения.")
    @Size(min = 3, max = 50, message = "Имя пользователя должно содержать от 3 до 50 символов.")
    private String username;

    @NotNull(message = "Пароль обязателен для заполнения.")
    @Size(min = 6, message = "Пароль должен содержать не менее 8 символов.")
    private String password;

    /**
     * Получить имя пользователя.
     * @return имя пользователя {@link String}
     */
    public String getUsername() {
        return username;
    }

    /**
     * Установить имя пользователя.
     * @param username имя пользователя
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Получить пароль пользователя.
     * @return пароль пользователя {@link String}
     */
    public String getPassword() {
        return password;
    }

    /**
     * Установить пароль пользователя.
     * @param password пароль пользователя
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
