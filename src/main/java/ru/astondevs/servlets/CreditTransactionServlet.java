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
import ru.astondevs.service.RequestService;
import ru.astondevs.service.ServiceLocator;
import ru.astondevs.service.TransactionService;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Сервлет для обработки кредитных транзакций.
 * При успешном выполнении кредитной транзакции, отправляет ответ с статусом 201 (CREATED)
 * и JSON-сообщением о успехе. В случае ошибки возвращает статус 400 (BAD REQUEST)
 * с сообщением об ошибке.
 */
@WebServlet(name = "CreditTransactionServlet", urlPatterns = {"/transactions/credit"})
public class CreditTransactionServlet extends HttpServlet {

    private final TransactionService transactionService;
    private final ObjectMapper mapper = new ObjectMapper();
    private final AuthService authService;
    private final RequestService requestService;

    /**
     * Конструктор по умолчанию для создания сервлета.
     * Использует сервисы из {@link ServiceLocator} для инициализации.
     */
    public CreditTransactionServlet() {
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
    public CreditTransactionServlet(TransactionService transactionService,
                                    AuthService authService,
                                    RequestService requestService) {
        this.transactionService = transactionService;
        this.authService = authService;
        this.requestService = requestService;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Player player = authService.getPlayerFromRequest(req, resp).orElse(null);
        if (player == null) {
            return;
        }
        BigDecimal creditAmount = requestService.getAmountFromRequest(req, resp).orElse(null);
        if (creditAmount == null) {
            return;
        }
        try {
            Transaction savedTransaction = transactionService.addCreditTransaction(player, creditAmount);
            TransactionDTO transactionDTO = TransactionMapper.INSTANCE.toDTO(savedTransaction);
            String jsonResponse = mapper.writeValueAsString(transactionDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Ошибка при выполнении транзакции\"}");
        }
    }
}
