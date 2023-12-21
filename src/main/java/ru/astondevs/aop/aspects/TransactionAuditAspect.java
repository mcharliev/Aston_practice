package ru.astondevs.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.astondevs.exception.NotEnoughMoneyException;
import ru.astondevs.model.entity.Player;
import ru.astondevs.model.entity.Transaction;

import java.math.BigDecimal;

/**
 * Аспект для аудита транзакций с объектами типа {@link Transaction}.
 */
@Aspect
public class TransactionAuditAspect {
    private static final Logger log = LoggerFactory.getLogger(TransactionAuditAspect.class);

    /**
     * Определение точки среза для дебетовых транзакций.
     */
    @Pointcut("execution(* ru.astondevs.service.TransactionServiceImpl.addDebitTransaction(..))")
    public void debitTransactionMethod() {}

    /**
     * Логирование процесса дебетовых транзакций.
     *
     * @param joinPoint соединительная точка
     * @param player игрок
     * @param debitAmount сумма дебета
     * @return результат выполнения метода, тип {@link Object}
     * @throws Throwable возможное исключение {@link Throwable}
     */
    @Around("debitTransactionMethod() && args(player, debitAmount)")
    public Object logDebitTransaction(ProceedingJoinPoint joinPoint,
                                      Player player,
                                      BigDecimal debitAmount) throws Throwable {
        log.info("Попытка дебетовой транзакции для пользователя " +
                player.getUsername() + " на сумму: " + debitAmount);
        try {
            Object result = joinPoint.proceed();
            log.info("Пользователь " + player.getUsername() +
                    " успешно совершил дебетовую операцию на сумму: " + debitAmount);
            return result;
        } catch (NotEnoughMoneyException e) {
            log.error("Ошибка дебетовой операции, недостаточно средств на счету пользователя "
                    + player.getUsername());
            throw e;
        }
    }

    /**
     * Определение точки среза для кредитных транзакций.
     */
    @Pointcut("execution(* ru.astondevs.service.TransactionServiceImpl.addCreditTransaction(..))")
    public void creditTransactionMethod() {}

    /**
     * Логирование процесса кредитных транзакций.
     *
     * @param joinPoint соединительная точка
     * @param player игрок
     * @param creditAmount сумма кредита
     * @return результат выполнения метода, тип {@link Object}
     * @throws Throwable возможное исключение {@link Throwable}
     */
    @Around("creditTransactionMethod() && args(player, creditAmount)")
    public Object logCreditTransaction(ProceedingJoinPoint joinPoint,
                                       Player player,
                                       BigDecimal creditAmount) throws Throwable {
        log.info("Попытка кредитной транзакции для пользователя " +
                player.getUsername() + " на сумму: " + creditAmount);
        Object result = joinPoint.proceed();
        log.info("Пользователь " + player.getUsername() +
                " успешно совершил кредитную операцию на сумму: " + creditAmount);
        return result;
    }

    /**
     * Определение точки среза для просмотра истории транзакций.
     */
    @Pointcut("execution(* ru.astondevs.service.TransactionServiceImpl.viewTransactionHistory(..))")
    public void viewTransactionHistoryMethod() {}

    /**
     * Логирование процесса просмотра истории транзакций.
     *
     * @param joinPoint соединительная точка
     * @param id ID транзакции
     * @param username имя пользователя
     * @return результат выполнения метода, тип {@link Object}
     * @throws Throwable возможное исключение {@link Throwable}
     */
    @Around("viewTransactionHistoryMethod() && args(id, username)")
    public Object logViewTransactionHistory(ProceedingJoinPoint joinPoint, Long id, String username) throws Throwable {
        log.info("Попытка просмотра истории транзакций для пользователя " + username);
        Object result = joinPoint.proceed();
        log.info("Пользователь " + username + " успешно просмотрел свою историю транзакций");
        return result;
    }
}

