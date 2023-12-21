package ru.astondevs.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.model.dto.TransactionDTO;
import ru.astondevs.model.entity.Player;
import ru.astondevs.model.entity.Transaction;
import ru.astondevs.model.mapper.TransactionMapper;
import ru.astondevs.service.AuthService;
import ru.astondevs.service.PlayerService;
import ru.astondevs.service.ServiceLocator;
import ru.astondevs.service.TransactionService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Сервлет, предназначенный для получения истории транзакций указанного игрока.
 * <p>
 * Если у игрока нет истории транзакций, возвращает JSON-ответ с сообщением о том, что история транзакций отсутствует.
 * В противном случае возвращает историю транзакций игрока в формате JSON.
 * </p>
 */
@WebServlet(name = "TransactionHistoryServlet", urlPatterns = {"/transactions/history"})
public class TransactionHistoryServlet extends HttpServlet {

    private PlayerService playerService;
    private AuthService authService;
    private ObjectMapper mapper = new ObjectMapper();
    private TransactionService transactionService;

    /**
     * Конструктор по умолчанию для создания сервлета.
     * Инициализирует сервисы игрока, аутентификации и транзакций с использованием {@link ServiceLocator}.
     */
    public TransactionHistoryServlet() {
        this(ServiceLocator.getPlayerService(), ServiceLocator.getAuthService(), ServiceLocator.getTransactionService());
    }

    /**
     * Конструктор, используемый для создания сервлета с явно указанными сервисами игрока, аутентификации и транзакций.
     *
     * @param playerService     Сервис для управления данными игрока.
     * @param authService       Сервис аутентификации.
     * @param transactionService Сервис для управления данными транзакций.
     */
    public TransactionHistoryServlet(PlayerService playerService, AuthService authService, TransactionService transactionService) {
        this.playerService = playerService;
        this.authService = authService;
        this.transactionService = transactionService;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Player> playerOpt = authService.getPlayerFromRequest(req, resp);
        if (playerOpt.isEmpty()) {
            return;
        }
        Player player = playerOpt.get();
        List<Transaction> transactions = transactionService.viewTransactionHistory(player.getId(), player.getUsername());
        List<TransactionDTO> dtoList = transactions.stream()
                .map(TransactionMapper.INSTANCE::toDTO).toList();

        resp.setContentType("application/json");

        if (transactions.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            String jsonResponse = mapper.writeValueAsString(dtoList);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonResponse);
        }
    }
}

