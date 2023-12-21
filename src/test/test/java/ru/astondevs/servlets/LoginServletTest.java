package ru.astondevs.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.astondevs.service.PlayerService;
import ru.astondevs.exception.AuthenticationException;
import ru.astondevs.model.dto.LoginResponseDTO;
import ru.astondevs.model.dto.RegisterPlayerDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {
    @Mock
    private PlayerService playerService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private PrintWriter writer;

    private LoginServlet servlet;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        servlet = new LoginServlet(playerService);
        when(resp.getWriter()).thenReturn(writer);
    }

    @Test
    void doPost_successfulLogin() throws IOException {
        // Предположим, что у нас есть действующие учетные данные
        String validUsername = "testUser";
        String validPassword = "testPassword";

        // Устанавливаем эти учетные данные в registerPlayerDTO
        RegisterPlayerDTO registerPlayerDTO = new RegisterPlayerDTO();
        registerPlayerDTO.setUsername(validUsername);
        registerPlayerDTO.setPassword(validPassword);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        // Преобразуем DTO в JSON для отправки через req.getReader()
        String jsonData = mapper.writeValueAsString(registerPlayerDTO);
        StringReader stringReader = new StringReader(jsonData);
        when(req.getReader()).thenReturn(new BufferedReader(stringReader));

        // Мокируем вызов сервиса с этими действующими учетными данными
        when(playerService.authenticateAndGenerateToken(validUsername, validPassword)).thenReturn(loginResponseDTO);

        // Вызываем тестируемый метод
        servlet.doPost(req, resp);

        // Проверяем, что статус ответа и ответ правильные
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(mapper.writeValueAsString(loginResponseDTO));
    }

    @Test
    void doPost_failedLogin() throws IOException {
        String jsonData = "{}";
        StringReader stringReader = new StringReader(jsonData);
        when(req.getReader()).thenReturn(new BufferedReader(stringReader));
        when(playerService.authenticateAndGenerateToken(null, null))
                .thenThrow(new AuthenticationException("Failed to authenticate"));

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write(mapper.writeValueAsString(Map.of("ошибка", "Failed to authenticate")));
    }
}
