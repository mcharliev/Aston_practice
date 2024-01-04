package ru.astondevs.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.astondevs.model.entity.Player;
import ru.astondevs.model.enums.PlayerActionType;
import ru.astondevs.service.PlayerAuditService;


/**
 * Аспект для аудита действий игрока с транзакциями
 */
@Aspect
@Component
public class TransactionAuditAspect {

    private static final Logger log = LoggerFactory.getLogger(TransactionAuditAspect.class);

    private final PlayerAuditService playerAuditService;

    @Autowired
    public TransactionAuditAspect(PlayerAuditService playerAuditService) {
        this.playerAuditService = playerAuditService;
    }

    /**
     * Точка среза для метода addDebitTransaction
     */
    @Pointcut("execution(* ru.astondevs.service.impl.TransactionServiceImpl.addDebitTransaction(..))")
    public void debitTransactionMethod() {}

    /**
     * Аспект для аудита дебетовых транзакций.
     *
     * @param joinPoint Присоединяемая точка выполнения.
     * @return Результат выполнения метода, тип {@link Object}.
     * @throws Throwable возможное исключение, тип {@link Throwable}.
     */
    @Around("debitTransactionMethod()")
    public Object logDebitTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();
        if (args[0] instanceof Player player) {
            logAction(player.getUsername(), PlayerActionType.DEBIT_TRANSACTION_SUCCESS);
            playerAuditService.logPlayerAction(player.getId(), PlayerActionType.DEBIT_TRANSACTION_SUCCESS.toString(),
                    createLogMessage(player.getUsername(), "успешно совершил дебетовую операцию"));
        }
        return result;
    }

    /**
     * Точка среза для метода addCreditTransaction
     */
    @Pointcut("execution(* ru.astondevs.service.impl.TransactionServiceImpl.addCreditTransaction(..))")
    public void creditTransactionMethod() {}

    /**
     * Аспект для аудита кредитных транзакций.
     *
     * @param joinPoint Присоединяемая точка выполнения.
     * @return Результат выполнения метода, тип {@link Object}.
     * @throws Throwable возможное исключение, тип {@link Throwable}.
     */
    @Around("creditTransactionMethod()")
    public Object logCreditTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();
        if (args[0] instanceof Player player) {
            logAction(player.getUsername(), PlayerActionType.CREDIT_TRANSACTION_SUCCESS);
            playerAuditService.logPlayerAction(player.getId(), PlayerActionType.CREDIT_TRANSACTION_SUCCESS.toString(),
                    createLogMessage(player.getUsername(), "успешно совершил кредитную операцию"));
        }
        return result;
    }

    /**
     * Точка среза для метода viewTransactionHistory
     */
    @Pointcut("execution(* ru.astondevs.service.impl.TransactionServiceImpl.viewTransactionHistory(..))")
    public void viewTransactionHistoryMethod() {}

    /**
     * Аспект для аудита просмотра истории транзакций.
     *
     * @param joinPoint Присоединяемая точка выполнения.
     * @return Результат выполнения метода, тип {@link Object}.
     * @throws Throwable возможное исключение, тип {@link Throwable}.
     */
    @Around("viewTransactionHistoryMethod()")
    public Object logViewTransactionHistory(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();
        if (args[0] instanceof Player player) {
            logAction(player.getUsername(), PlayerActionType.VIEW_TRANSACTION_HISTORY);
            playerAuditService.logPlayerAction(player.getId(), PlayerActionType.VIEW_TRANSACTION_HISTORY.toString(),
                    createLogMessage(player.getUsername(), "просмотрел свою историю транзакций"));
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
        log.info(createLogMessage(username, actionType.getAction()));
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

