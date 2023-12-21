package ru.astondevs.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.exception.ConflictException;
import ru.astondevs.exception.ValidationException;
import ru.astondevs.model.dto.PlayerDTO;
import ru.astondevs.model.dto.RegisterPlayerDTO;
import ru.astondevs.service.PlayerService;
import ru.astondevs.service.ServiceLocator;

import java.io.IOException;

/**
 * Сервлет, предназначенный для регистрации новых пользователей.
 * <p>
 * После успешной регистрации пользователь получает JSON-ответ с сообщением об успехе и данными нового пользователя.
 * В случае ошибки или если пользователь с таким именем уже существует, возвращает соответствующее сообщение об ошибке.
 * </p>
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/players/register"})
public class RegisterServlet extends HttpServlet {

    private PlayerService playerService;
    private ObjectMapper mapper;

    /**
     * Конструктор по умолчанию для создания сервлета.
     * Инициализирует объект ObjectMapper и использует {@link ServiceLocator} для получения сервиса игрока.
     */
    public RegisterServlet() {
        mapper = new ObjectMapper();
        this.playerService = ServiceLocator.getPlayerService();
    }

    /**
     * Конструктор, используемый для создания сервлета с явно указанными сервисами игрока и объектом ObjectMapper.
     *
     * @param playerService Сервис для управления данными игрока.
     * @param mapper        Объект ObjectMapper для обработки JSON.
     */
    public RegisterServlet(PlayerService playerService, ObjectMapper mapper) {
        this.playerService = playerService;
        this.mapper = mapper;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            RegisterPlayerDTO registerPlayerDTO = mapper.readValue(req.getReader(), RegisterPlayerDTO.class);
            PlayerDTO registeredPlayerDTO = playerService.registerNewPlayer(registerPlayerDTO);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(mapper.writeValueAsString(registeredPlayerDTO));
        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        } catch (ConflictException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(e.getMessage());
        }
    }
}
