package ru.astondevs.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.exception.AuthenticationException;
import ru.astondevs.model.dto.LoginResponseDTO;
import ru.astondevs.model.dto.RegisterPlayerDTO;
import ru.astondevs.model.entity.Player;
import ru.astondevs.model.mapper.PlayerMapper;
import ru.astondevs.service.PlayerService;
import ru.astondevs.service.ServiceLocator;

import java.io.IOException;
import java.util.Map;

/**
 * Сервлет, предназначенный для авторизации пользователей.
 * <p>
 * После успешной авторизации пользователь получает JSON-ответ с сообщением об успехе, данными пользователя
 * и JWT-токеном. В случае ошибки авторизации возвращает сообщение об ошибке.
 * </p>
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/players/login"})
public class LoginServlet extends HttpServlet {
    private PlayerService playerService;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Конструктор, используемый для создания сервлета с явно указанным сервисом игрока.
     *
     * @param playerService Сервис для управления данными игрока.
     */
    public LoginServlet(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Конструктор по умолчанию для создания сервлета.
     * Использует сервис из {@link ServiceLocator} для инициализации.
     */
    public LoginServlet() {}


    @Override
    public void init() {
        if (playerService == null) {
            this.playerService = ServiceLocator.getPlayerService();
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            RegisterPlayerDTO registerPlayerDTO = mapper.readValue(req.getReader(), RegisterPlayerDTO.class);
            Player playerEntity = PlayerMapper.INSTANCE.toPlayerEntity(registerPlayerDTO);
            LoginResponseDTO response = playerService.authenticateAndGenerateToken(playerEntity.getUsername(),
                    playerEntity.getPassword());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(response));
        } catch (AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write(mapper.writeValueAsString(Map.of("ошибка", e.getMessage())));
        }
    }
}