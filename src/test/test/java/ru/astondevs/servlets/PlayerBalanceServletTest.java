package ru.astondevs.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.service.PlayerService;
import ru.astondevs.model.entity.Player;
import ru.astondevs.service.AuthService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerBalanceServletTest {

    @Mock
    private PlayerService playerService;
    @Mock
    private AuthService authService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private PrintWriter writer;

    private PlayerBalanceServlet servlet;

    @BeforeEach
    void setUp() throws IOException {
        servlet = new PlayerBalanceServlet(playerService, authService);
        when(resp.getWriter()).thenReturn(writer);
    }
    @Test
    void doGet_successfulBalanceFetch() throws IOException {
        Player player = new Player("testUser", "testPass");
        String jsonResponse = "{\"balance\": 1500}";

        when(authService.getPlayerFromRequest(req, resp)).thenReturn(Optional.of(player));
        when(playerService.getPlayerBalanceInfo(player)).thenReturn(jsonResponse);

        servlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(jsonResponse);
    }
    @Test
    void doGet_playerNotFound() throws IOException {
        lenient().when(resp.getWriter()).thenReturn(writer);
        when(authService.getPlayerFromRequest(req, resp)).thenReturn(Optional.empty());

        servlet.doGet(req, resp);

        verify(writer, never()).write(anyString());
    }
    @Test
    void doGet_errorFetchingBalance() throws IOException {
        Player player = new Player("testUser", "testPass");

        when(authService.getPlayerFromRequest(req, resp)).thenReturn(Optional.of(player));
        when(playerService.getPlayerBalanceInfo(player)).thenThrow(new RuntimeException());

        servlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write("{\"error\": \"Ошибка при получении баланса игрока\"}");
    }
}