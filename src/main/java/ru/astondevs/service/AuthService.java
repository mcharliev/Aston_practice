package ru.astondevs.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.model.entity.Player;
import ru.astondevs.util.JwtUtil;

import java.io.IOException;
import java.util.Optional;

/**
 * Класс, предоставляющий функциональность аутентификации и авторизации игроков.
 */
public class AuthService {

    private final JwtUtil jwtUtil;
    private final PlayerService playerService;

    /**
     * Конструктор класса AuthService.
     *
     * @param playerService Сервис игроков, см. {@link PlayerService}.
     * @param jwtUtil Утилита для работы с JWT-токенами, см. {@link JwtUtil}.
     */
    public AuthService(PlayerService playerService, JwtUtil jwtUtil) {
        this.playerService = playerService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Получает игрока из запроса, основываясь на JWT-токене.
     *
     * @param req  HTTP-запрос от клиента, см. {@link HttpServletRequest}.
     * @param resp HTTP-ответ сервера, см. {@link HttpServletResponse}.
     * @return {@link Optional} объект игрока, если аутентификация прошла успешно, иначе пустой {@link Optional}.
     * @throws IOException если произошла ошибка ввода-вывода при отправке HTTP-ответа.
     */
    public Optional<Player> getPlayerFromRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getHeader("Authorization");
        Optional<Player> playerOpt = validateTokenAndGetPlayer(token);
        if (playerOpt.isEmpty()) {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Токен отсутствует или недействителен\"}");
        }
        return playerOpt;
    }

    /**
     * Проверяет JWT-токен, извлекает из него имя пользователя и валидирует его.
     *
     * @param token JWT-токен для валидации.
     * @return {@link Optional} объект игрока, если аутентификация прошла успешно, иначе пустой {@link Optional}.
     */
    private Optional<Player> validateTokenAndGetPlayer(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Optional.empty();
        }
        token = token.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            if (!jwtUtil.validateToken(token, username)) {
                return Optional.empty();
            }
            return playerService.findPlayerByUsername(username);
        } catch (SignatureException | ExpiredJwtException ex) {
            return Optional.empty();
        }
    }
}

