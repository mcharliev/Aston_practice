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
import ru.astondevs.exception.ValidationException;
import ru.astondevs.exception.ConflictException;
import ru.astondevs.model.dto.PlayerDTO;
import ru.astondevs.model.dto.RegisterPlayerDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterServletTest {

    @Mock
    private PlayerService playerService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private BufferedReader reader;
    @Mock
    private PrintWriter writer;

    private RegisterServlet servlet;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        servlet = new RegisterServlet(playerService, mapper);
        when(req.getReader()).thenReturn(reader);
        when(resp.getWriter()).thenReturn(writer);
    }

    @Test
    void doPost_successfulRegistration() throws IOException {
        RegisterPlayerDTO registerPlayerDTO = new RegisterPlayerDTO();
        PlayerDTO playerDTO = new PlayerDTO();

        String jsonData = mapper.writeValueAsString(registerPlayerDTO);
        StringReader stringReader = new StringReader(jsonData);
        when(req.getReader()).thenReturn(new BufferedReader(stringReader));
        when(playerService.registerNewPlayer(any(RegisterPlayerDTO.class))).thenReturn(playerDTO);
        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_CREATED);
        verify(writer).write(mapper.writeValueAsString(playerDTO));
    }

    @Test
    void doPost_validationError() throws IOException {
        String jsonData = "{}";
        StringReader stringReader = new StringReader(jsonData);
        when(req.getReader()).thenReturn(new BufferedReader(stringReader));
        when(playerService.registerNewPlayer(any())).thenThrow(new ValidationException("Validation error"));

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write("Validation error");
    }

    @Test
    void doPost_conflictError() throws IOException {
        String jsonData = "{}";
        StringReader stringReader = new StringReader(jsonData);
        when(req.getReader()).thenReturn(new BufferedReader(stringReader));
        when(playerService.registerNewPlayer(any())).thenThrow(new ConflictException("Conflict error"));

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_CONFLICT);
        verify(writer).write("Conflict error");
    }
}
