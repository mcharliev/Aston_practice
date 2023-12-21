package ru.astondevs.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.exception.NotEnoughMoneyException;
import ru.astondevs.model.dto.TransactionDTO;
import ru.astondevs.model.entity.Player;
import ru.astondevs.model.entity.Transaction;
import ru.astondevs.model.mapper.TransactionMapper;
import ru.astondevs.service.AuthService;
import ru.astondevs.service.RequestService;
import ru.astondevs.service.ServiceLocator;
import ru.astondevs.service.TransactionService;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Сервлет для обработки дебетовых транзакций.
 * При успешной обработке дебетовой транзакции, отправляет ответ с статусом 201 (CREATED)
 * и JSON-сообщением о успехе. Если на счете недостаточно средств для проведения дебетовой транзакции,
 * возвращает статус 400 (BAD REQUEST) с сообщением о недостатке средств.
 */
@WebServlet(name = "DebitTransactionServlet", urlPatterns = {"/transactions/debit"})
public class DebitTransactionServlet extends HttpServlet {
    private TransactionService transactionService;
    private ObjectMapper mapper = new ObjectMapper();
    private AuthService authService;
    private RequestService requestService;

    /**
     * Конструктор по умолчанию для создания сервлета.
     * Использует сервисы из {@link ServiceLocator} для инициализации.
     */
    public DebitTransactionServlet() {
        this(ServiceLocator.getTransactionService(),
                ServiceLocator.getAuthService(),
                ServiceLocator.getRequestService());
    }

    /**
     * Конструктор для создания сервлета с явным указанием сервисов.
     *
     * @param transactionService Сервис для управления транзакциями.
     * @param authService        Сервис для аутентификации игрока.
     * @param requestService     Сервис для обработки запросов.
     */
    public DebitTransactionServlet(TransactionService transactionService,
                                   AuthService authService,
                                   RequestService requestService) {
        this.transactionService = transactionService;
        this.authService = authService;
        this.requestService = requestService;
    }


    @Override
    public void init() {
        this.transactionService = ServiceLocator.getTransactionService();
        this.authService = ServiceLocator.getAuthService();
        this.requestService = ServiceLocator.getRequestService();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Player player = authService.getPlayerFromRequest(req, resp).orElse(null);
        if (player == null) {
            return;
        }
        BigDecimal debitAmount = requestService.getAmountFromRequest(req, resp).orElse(null);
        if (debitAmount == null) {
            return;
        }
        try {
            Transaction savedTransaction = transactionService.addDebitTransaction(player, debitAmount);
            TransactionDTO transactionDTO = TransactionMapper.INSTANCE.toDTO(savedTransaction);
            String jsonResponse = mapper.writeValueAsString(transactionDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(jsonResponse);
        } catch (NotEnoughMoneyException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Недостаточно средств на счете\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Ошибка при выполнении транзакции\"}");
        }
    }
}