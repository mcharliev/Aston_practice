package ru.astondevs.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.astondevs.model.dto.LoginResponseDTO;
import ru.astondevs.model.dto.PlayerBalanceDTO;
import ru.astondevs.model.dto.PlayerDTO;
import ru.astondevs.model.dto.RegisterPlayerDTO;
import ru.astondevs.model.entity.Player;
import ru.astondevs.model.enums.PlayerActionType;
import ru.astondevs.service.PlayerAuditService;


/**
 * Аспект для аудита действий игрока
 */
@Aspect
@Component
public class PlayerAuditAspect {

    private static final Logger log = LoggerFactory.getLogger(PlayerAuditAspect.class);

    private final PlayerAuditService playerAuditService;

    @Autowired
    public PlayerAuditAspect(PlayerAuditService playerAuditService) {
        this.playerAuditService = playerAuditService;
    }

    /**
     * Точка среза для метода registerNewPlayer.
     */
    @Pointcut("execution(* ru.astondevs.service.impl.PlayerServiceImpl.registerNewPlayer(..))")
    public void registerNewPlayer() {
    }

    /**
     * Аспект для аудита регистрации нового игрока.
     *
     * @param joinPoint Присоединяемая точка выполнения.
     * @return Результат выполнения метода, тип {@link Object}.
     * @throws Throwable возможное исключение, тип {@link Throwable}.
     */
    @Around("registerNewPlayer()")
    public Object logRegistration(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        if (result instanceof PlayerDTO playerDTO) {
            logAction(playerDTO.getUsername(), PlayerActionType.REGISTRATION);
            playerAuditService.logPlayerAction(playerDTO.getId(), PlayerActionType.REGISTRATION.toString(), createLogMessage(playerDTO.getUsername(), "успешно зарегистрировался"));
        }
        return result;
    }

    /**
     * Точка среза для метода authenticateAndGenerateToken
     */
    @Pointcut("execution(* ru.astondevs.service.impl.PlayerServiceImpl.authenticateAndGenerateToken(..))")
    public void authenticateAndGenerateToken() {
    }

    /**
     * Аспект для аудита аутентификации и генерации токена.
     *
     * @param joinPoint Присоединяемая точка выполнения.
     * @return Результат выполнения метода, тип {@link Object}.
     * @throws Throwable возможное исключение, тип {@link Throwable}.
     */
    @Around("authenticateAndGenerateToken()")
    public Object logLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String username = null;
        if (args[0] instanceof RegisterPlayerDTO registerPlayerDTO) {
            username = registerPlayerDTO.getUsername();
        }

        Object result = joinPoint.proceed();
        if (result instanceof LoginResponseDTO responseDTO) {
            if (responseDTO.getPlayer() != null) {
                Long playerId = responseDTO.getPlayer().getId();
                if (username != null) {
                    logAction(username, PlayerActionType.AUTHENTICATION);
                    playerAuditService.logPlayerAction(playerId, PlayerActionType.AUTHENTICATION.toString(), createLogMessage(username, "прошел авторизацию"));
                }
            } else if (username != null) {
                log.error("Ошибка авторизации пользователя с именем " + username);
            }
        }
        return result;
    }

    /**
     * Точка среза для метода getPlayerBalanceInfo
     */
    @Pointcut("execution(* ru.astondevs.service.impl.PlayerServiceImpl.getPlayerBalanceInfo(..))")
    public void getPlayerBalanceInfoMethod() {
    }

    /**
     * Аспект для аудита запроса баланса игрока.
     *
     * @param joinPoint Присоединяемая точка выполнения.
     * @return Результат выполнения метода, тип {@link Object}.
     * @throws Throwable возможное исключение, тип {@link Throwable}.
     */
    @Around("getPlayerBalanceInfoMethod()")
    public Object logGetPlayerBalanceInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();
        if (args[0] instanceof Player player && result instanceof PlayerBalanceDTO) {
            logAction(player.getUsername(), PlayerActionType.BALANCE_CHECK);
            playerAuditService.logPlayerAction(player.getId(), PlayerActionType.BALANCE_CHECK.toString(), createLogMessage(player.getUsername(), "сделал запрос баланса"));
        }
        return result;
    }

    /**
     * Метод для логирования действий игрока.
     *
     * @param username   Имя пользователя.
     * @param actionType Тип действия игрока.
     */
    private void logAction(String username, PlayerActionType actionType) {
        switch (actionType) {
            case REGISTRATION -> log.info(createLogMessage(username, "успешно зарегистрировался"));
            case AUTHENTICATION -> log.info(createLogMessage(username, "прошел авторизацию"));
            case BALANCE_CHECK -> log.info(createLogMessage(username, "сделал запрос баланса"));
        }
    }

    /**
     * Метод для создания сообщения в логе.
     *
     * @param username Имя пользователя.
     * @param action   Действие игрока.
     * @return Сообщение для лога.
     */
    private String createLogMessage(String username, String action) {
        return "Пользователь " + username + " " + action;
    }
}
