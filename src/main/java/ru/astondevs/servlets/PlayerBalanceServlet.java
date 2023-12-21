package ru.astondevs.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.model.entity.Player;
import ru.astondevs.service.AuthService;
import ru.astondevs.service.PlayerService;
import ru.astondevs.service.ServiceLocator;

import java.io.IOException;


/**
 * Сервлет, предназначенный для получения текущего баланса игрока.
 * <p>
 * После успешной валидации токена и получения игрока, сервлет возвращает JSON-ответ с текущим балансом пользователя.
 * </p>
 */
@WebServlet(name = "PlayerBalanceServlet", urlPatterns = {"/players/balance"})
public class PlayerBalanceServlet extends HttpServlet {

    private final PlayerService playerService;
    private final AuthService authService;

    /**
     * Конструктор по умолчанию для создания сервлета.
     * Использует сервисы из {@link ServiceLocator} для инициализации.
     */
    public PlayerBalanceServlet() {
        this(ServiceLocator.getPlayerService(), ServiceLocator.getAuthService());
    }

    /**
     * Конструктор, используемый для создания сервлета с явно указанными сервисами игрока и аутентификации.
     *
     * @param playerService Сервис для управления данными игрока.
     * @param authService   Сервис аутентификации.
     */
    public PlayerBalanceServlet(PlayerService playerService, AuthService authService) {
        this.playerService = playerService;
        this.authService = authService;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Player player = authService.getPlayerFromRequest(req, resp).orElse(null);
        if (player == null) {
            return;
        }
        try {
            String jsonResponse = playerService.getPlayerBalanceInfo(player);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Ошибка при получении баланса игрока\"}");
        }
    }
}