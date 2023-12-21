package ru.astondevs.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.service.TransactionService;
import ru.astondevs.exception.NotEnoughMoneyException;
import ru.astondevs.model.mapper.TransactionMapper;
import ru.astondevs.model.entity.Player;
import ru.astondevs.model.entity.Transaction;
import ru.astondevs.model.enums.TransactionType;
import ru.astondevs.service.AuthService;
import ru.astondevs.service.RequestService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebitTransactionServletTest {

    @Mock
    private TransactionService transactionService;
    @Mock
    private AuthService authService;
    @Mock
    private RequestService requestService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private PrintWriter writer;

    private DebitTransactionServlet servlet;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        servlet = new DebitTransactionServlet(transactionService, authService, requestService);
        when(resp.getWriter()).thenReturn(writer);
    }
    @Test
    void doPost_successfulTransaction() throws IOException {
        Player player = new Player("testUser", "testPass");
        BigDecimal debitAmount = new BigDecimal("100.50");
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.DEBIT);
        transaction.setAmount(debitAmount);
        transaction.setLocalDateTime(LocalDateTime.now());

        when(authService.getPlayerFromRequest(req, resp)).thenReturn(Optional.of(player));
        when(requestService.getAmountFromRequest(req, resp)).thenReturn(Optional.of(debitAmount));
        when(transactionService.addDebitTransaction(player, debitAmount)).thenReturn(transaction);

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_CREATED);
        verify(writer).write(mapper.writeValueAsString(TransactionMapper.INSTANCE.toDTO(transaction)));
    }

    @Test
    void doPost_noPlayer() throws IOException {
        lenient().when(resp.getWriter()).thenReturn(writer);
        when(authService.getPlayerFromRequest(req, resp)).thenReturn(Optional.empty());

        servlet.doPost(req, resp);

        verify(writer, never()).write(anyString());
    }

    @Test
    void doPost_invalidDebitAmount() throws IOException {
        lenient().when(resp.getWriter()).thenReturn(writer);
        Player player = new Player("testUser", "testPass");

        when(authService.getPlayerFromRequest(req, resp)).thenReturn(Optional.of(player));
        when(requestService.getAmountFromRequest(req, resp)).thenReturn(Optional.empty());

        servlet.doPost(req, resp);

        verify(writer, never()).write(anyString());
    }

    @Test
    void doPost_notEnoughMoney() throws IOException {
        Player player = new Player("testUser", "testPass");
        BigDecimal debitAmount = new BigDecimal("100.50");

        when(authService.getPlayerFromRequest(req, resp)).thenReturn(Optional.of(player));
        when(requestService.getAmountFromRequest(req, resp)).thenReturn(Optional.of(debitAmount));
        when(transactionService.addDebitTransaction(player, debitAmount)).thenThrow(new NotEnoughMoneyException());

        when(resp.getWriter()).thenReturn(writer);

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write("{\"error\": \"Недостаточно средств на счете\"}");
    }

    @Test
    void doPost_transactionError() throws IOException {
        Player player = new Player("testUser", "testPass");
        BigDecimal debitAmount = new BigDecimal("100.50");

        when(authService.getPlayerFromRequest(req, resp)).thenReturn(Optional.of(player));
        when(requestService.getAmountFromRequest(req, resp)).thenReturn(Optional.of(debitAmount));
        when(transactionService.addDebitTransaction(player, debitAmount)).thenThrow(new RuntimeException());

        servlet.doPost(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write("{\"error\": \"Ошибка при выполнении транзакции\"}");
    }
}