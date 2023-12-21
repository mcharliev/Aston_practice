package ru.astondevs.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.service.PlayerService;
import ru.astondevs.service.TransactionService;
import ru.astondevs.model.entity.Player;
import ru.astondevs.model.entity.Transaction;
import ru.astondevs.service.AuthService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionHistoryServletTest {

    @Mock
    private PlayerService playerService;
    @Mock
    private AuthService authService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private PrintWriter writer;

    private TransactionHistoryServlet servlet;

    @BeforeEach
    void setUp() throws IOException {
        servlet = new TransactionHistoryServlet(playerService, authService, transactionService);
        when(resp.getWriter()).thenReturn(writer);
    }

    @Test
    void doGet_noPlayer() throws IOException {
        lenient().when(resp.getWriter()).thenReturn(writer);
        when(authService.getPlayerFromRequest(req, resp)).thenReturn(Optional.empty());

        servlet.doGet(req, resp);

        verify(resp, never()).setStatus(HttpServletResponse.SC_OK);
        verify(writer, never()).write(anyString());
    }

    @Test
    void doGet_noTransactions() throws IOException {
        lenient().when(resp.getWriter()).thenReturn(writer);
        Player player = new Player("testUser", "testPass");
        when(authService.getPlayerFromRequest(req, resp)).thenReturn(Optional.of(player));
        when(transactionService.viewTransactionHistory(player.getId(), player.getUsername())).thenReturn(Collections.emptyList());

        servlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_NO_CONTENT);
        verify(writer, never()).write(anyString());
    }

    @Test
    void doGet_withTransactions() throws IOException {
        Player player = new Player("testUser", "testPass");
        List<Transaction> transactions = List.of(new Transaction());
        when(authService.getPlayerFromRequest(req, resp)).thenReturn(Optional.of(player));
        when(transactionService.viewTransactionHistory(player.getId(), player.getUsername())).thenReturn(transactions);

        servlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(anyString());
    }
}
