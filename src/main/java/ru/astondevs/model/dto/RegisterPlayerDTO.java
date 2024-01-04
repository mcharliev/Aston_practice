package ru.astondevs.model.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



/**
 * Класс, представляющий собой объект передачи данных (DTO) для регистрации игрока.
 */
public class RegisterPlayerDTO {

    @NotNull(message = "Имя пользователя обязательно для заполнения.")
    @Size(min = 4, max = 50, message = "Имя пользователя должно содержать от 4 до 50 символов.")
    @ApiModelProperty(notes = "Имя пользователя", example = "Johny")
    private String username;

    @NotNull(message = "Пароль обязателен для заполнения.")
    @Size(min = 6, message = "Пароль должен содержать не менее 8 символов.")
    @ApiModelProperty(notes = "Пароль пользователя", example = "123456")
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
