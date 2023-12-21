package ru.astondevs.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.astondevs.model.dto.LoginResponseDTO;
import ru.astondevs.model.dto.PlayerDTO;
import ru.astondevs.model.entity.Player;

/**
 * Аспект для аудита действий с объектами типа {@link Player}.
 */
@Aspect
public class PlayerAuditAspect {
    private static final Logger log = LoggerFactory.getLogger(PlayerAuditAspect.class);

    /**
     * Определяет точку среза для метода
     * который представляет собой регистрацию нового игрока.
     */
    @Pointcut("execution(* ru.astondevs.service.PlayerServiceImpl.registerNewPlayer(..))")
    public void registerNewPlayer() {
    }

    /**
     * Ведет аудит операции регистрации нового игрока.
     *
     * @param joinPoint Точка присоединения, представляющая метод регистрации.
     * @return Результат выполнения метода регистрации.
     * @throws Throwable Если произошла ошибка при выполнении метода.
     */
    @Around("registerNewPlayer()")
    public Object logRegistration(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        if (result instanceof PlayerDTO) {
            PlayerDTO playerDTO = (PlayerDTO) result;
            log.info("Пользователь " + playerDTO.getUsername() + " успешно зарегистрировался");
        }
        return result;
    }

    /**
     * Определяет точку среза для метода
     * который представляет собой авторизацию и генерацию токена для пользователя.
     */
    @Pointcut("execution(* ru.astondevs.service.PlayerServiceImpl.authenticateAndGenerateToken(..))")
    public void authenticateAndGenerateToken() {
    }

    /**
     * Ведет аудит операции авторизации пользователя и генерации токена.
     *
     * @param joinPoint Точка присоединения, представляющая метод авторизации.
     * @return Результат выполнения метода авторизации.
     * @throws Throwable Если произошла ошибка при выполнении метода.
     */
    @Around("authenticateAndGenerateToken()")
    public Object logLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String username = (String) args[0];
        Object result = joinPoint.proceed();
        if (result instanceof LoginResponseDTO) {
            log.info("Пользователь " + username + " прошел авторизацию");
        } else {
            log.error("Ошибка авторизации пользователя с именем " + username);
        }
        return result;
    }

    /**
     * Определяет точку среза для метода
     * который представляет собой запрос баланса игрока.
     */
    @Pointcut("execution(* ru.astondevs.service.PlayerServiceImpl.getPlayerBalanceInfo(..))")
    public void getPlayerBalanceInfoMethod() {
    }

    /**
     * Ведет аудит операции запроса баланса игрока.
     *
     * @param joinPoint Точка присоединения, представляющая метод запроса баланса игрока.
     * @return Результат выполнения метода запроса баланса игрока.
     * @throws Throwable Если произошла ошибка при выполнении метода.
     */
    @Around("getPlayerBalanceInfoMethod()")
    public Object logGetPlayerBalanceInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Player player = (Player) args[0];
        Object result = joinPoint.proceed();
        if (result instanceof String) {
            log.info("Запрос баланса для игрока " + player.getUsername() + ": " + result);
        }
        return result;
    }
}
