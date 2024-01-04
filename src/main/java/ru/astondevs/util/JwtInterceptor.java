package ru.astondevs.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.astondevs.exception.JwtException;
import ru.astondevs.model.entity.Player;
import ru.astondevs.service.PlayerService;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Класс-интерцептор для обработки JWT-токенов.
 * Этот интерцептор проверяет наличие и валидность JWT-токена в заголовке запроса.
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final PlayerService playerService;

    /**
     * Конструктор с внедрением зависимости {@link PlayerService}.
     *
     * @param playerService сервис для работы с игроками, тип {@link PlayerService}.
     */
    public JwtInterceptor(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Метод выполняется перед обработкой HTTP-запроса.
     * Проверяет наличие и валидность JWT-токена в заголовке запроса. Если токен отсутствует
     * или недействителен, выбрасывается исключение {@link JwtException}.
     *
     * @param request  HTTP-запрос, тип {@link HttpServletRequest}.
     * @param response HTTP-ответ, тип {@link HttpServletResponse}.
     * @param handler  обработчик запроса, объект Object
     * @return  true, если токен валиден и запрос должен продолжить выполнение;
     *         в противном случае, false.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Optional<Player> playerOpt = playerService.validateTokenAndGetPlayer(request.getHeader("Authorization"));
        if (playerOpt.isEmpty()) {
            throw new JwtException();
        }
        request.setAttribute("authenticatedPlayer", playerOpt.get());
        return true;
    }
}

